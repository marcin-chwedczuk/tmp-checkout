package pl.marcinchwedczuk.checkout3.checkout.application;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import pl.marcinchwedczuk.checkout3.checkout.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.*;
import static pl.marcinchwedczuk.checkout3.checkout.domain.BigDecimals.summingBigDecimal;

@Service
public class CheckoutService {
	public CheckoutService(
			ItemRepository itemRepository,
			QuantityPricingRuleRepository quantityPricingRuleRepository,
			PricingCalculator pricingCalculator) {
		this.itemRepository = itemRepository;
		this.quantityPricingRuleRepository = quantityPricingRuleRepository;
		this.pricingCalculator = pricingCalculator;
	}

	private final ItemRepository itemRepository;
	private final QuantityPricingRuleRepository quantityPricingRuleRepository;
	private final PricingCalculator pricingCalculator;

	public CheckoutResponseDTO computePrices(CheckoutRequestDTO checkoutRequest) {

		Map<String, BigDecimal> quantityByItemNumber = checkoutRequest.getLines().stream()
				.collect(groupingBy(LineDTO::getItemNumber,
						mapping(LineDTO::getQuantity, summingBigDecimal())));

		List<Item> items = itemRepository.findAllByNumberIn(quantityByItemNumber.keySet());
		assertHavePricesForAllItems(items, checkoutRequest.getLines());

		List<ItemPricingData> itemPricingDataList = items.stream()
				.map(item -> createPricingData(item, quantityByItemNumber))
				.collect(toList());

		// 1. Apply quantity rules
		LocalDateTime checkoutTime = checkoutRequest.getRequestTime();

		for (ItemPricingData pricingData : itemPricingDataList) {
			applyQuantityDiscount(checkoutTime, pricingData);
		}


		CheckoutResponseDTO checkoutResponseDTO =
				createResponse(checkoutRequest, itemPricingDataList);
		return checkoutResponseDTO;
	}

	private ItemPricingData createPricingData(Item item, Map<String, BigDecimal> quantityByItemNumber) {
		BigDecimal requestedQuantity =
				quantityByItemNumber.get(item.getNumber());

		return ItemPricingData.fromItemAndQuantity(item, requestedQuantity);
	}

	private void assertHavePricesForAllItems(List<Item> items, List<LineDTO> checkoutLines) {
		Set<String> requiredItemNumbers = checkoutLines.stream()
				.map(LineDTO::getItemNumber)
				.collect(toSet());

		Set<String> foundItemNumbers = items.stream()
				.map(Item::getNumber)
				.collect(toSet());

		Set<String> missing = Sets.difference(requiredItemNumbers, foundItemNumbers);

		if (!missing.isEmpty()) {
			throw new CheckoutException(
					"Missing pricing information for item(s): '" +
					missing.stream().collect(joining("', '")) +
					"'. Please check service configuration.");
		}
	}

	private void applyQuantityDiscount(LocalDateTime checkoutTime, ItemPricingData pricingData) {
		List<QuantityDiscountRule> applicableRules =
				quantityPricingRuleRepository.findApplicableRules(
					pricingData.getItem(),
					pricingData.getQuantity(),
					checkoutTime);

		if (applicableRules.size() > 1)
			throw new RuntimeException(
					"Invalid configuration for item: " +
					pricingData.getItem().getNumber() + ". " +
					"Found more than one applicable quantity rule.");

		if (!applicableRules.isEmpty()) {
			QuantityDiscountRule rule = applicableRules.get(0);

			BigDecimal discountedPrice = pricingCalculator.computeDiscountedPrice(
					pricingData.getOriginalUnitPrice(), rule);

			pricingData.setDiscountedPrice(discountedPrice);
		}
		else {
			pricingData.setDiscountedPrice(pricingData.getOriginalUnitPrice());
		}
	}

	private CheckoutResponseDTO createResponse(
			CheckoutRequestDTO checkoutRequest, List<ItemPricingData> pricingDataList) {

		CheckoutResponseDTO responseDTO = new CheckoutResponseDTO();

		responseDTO.setRequestTime(checkoutRequest.getRequestTime());
		responseDTO.setLines(new ArrayList<>());

		for (ItemPricingData pricingData : pricingDataList) {
			PricedLineDTO pricedLineDTO = new PricedLineDTO();

			pricedLineDTO.setOriginalUnitPrice(pricingData.getOriginalUnitPrice());
			pricedLineDTO.setFinalUnitPrice(pricingData.getDiscountedPrice());

			pricedLineDTO.setItemNumber(pricingData.getItem().getNumber());
			pricedLineDTO.setQuantity(pricingData.getQuantity());

			responseDTO.getLines().add(pricedLineDTO);
		}

		return responseDTO;
	}
}
