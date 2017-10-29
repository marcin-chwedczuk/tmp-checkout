package pl.marcinchwedczuk.checkout3.checkout.application;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import pl.marcinchwedczuk.checkout3.checkout.domain.*;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.CheckoutPricingData;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.DoubleSellDiscountApplier;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.ItemPricingData;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.QuantityDiscountApplier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals.summingBigDecimal;

@Service
public class CheckoutService {
	public CheckoutService(
			ItemRepository itemRepository,
			QuantityDiscountRuleRepository quantityPricingRuleRepository,
			DoubleSellDiscountRuleRepository doubleSellDiscountRuleRepository,
			DoubleSellDiscountApplier doubleSellDiscountApplier,
			QuantityDiscountApplier quantityDiscountApplier)
	{
		this.itemRepository = itemRepository;
		this.quantityPricingRuleRepository = quantityPricingRuleRepository;
		this.doubleSellDiscountRuleRepository = doubleSellDiscountRuleRepository;
		this.doubleSellDiscountApplier = doubleSellDiscountApplier;
		this.quantityDiscountApplier = quantityDiscountApplier;
	}

	private final ItemRepository itemRepository;
	private final QuantityDiscountRuleRepository quantityPricingRuleRepository;
	private final DoubleSellDiscountRuleRepository doubleSellDiscountRuleRepository;
	private final DoubleSellDiscountApplier doubleSellDiscountApplier;
	private final QuantityDiscountApplier quantityDiscountApplier;

	public CheckoutResponseDTO computePrices(CheckoutRequestDTO checkoutRequest) {
		CheckoutPricingData checkoutPricingData =
				createCheckoutPricingDataFromRequest(checkoutRequest);

		LocalDateTime checkoutTime = checkoutRequest.getRequestTime();

		applyQuantityDiscounts(checkoutTime, checkoutPricingData);
		applyDoubleSellDiscounts(checkoutTime, checkoutPricingData);

		CheckoutResponseDTO checkoutResponseDTO =
				createResponse(checkoutRequest, checkoutPricingData);
		return checkoutResponseDTO;
	}

	private CheckoutPricingData createCheckoutPricingDataFromRequest(CheckoutRequestDTO checkoutRequest) {
		Map<String, BigDecimal> quantityByItemNumber = checkoutRequest.getLines().stream()
				.collect(groupingBy(LineDTO::getItemNumber,
						mapping(LineDTO::getQuantity, summingBigDecimal())));

		List<Item> items = itemRepository.findAllByNumberIn(quantityByItemNumber.keySet());
		assertHavePricesForAllItems(items, checkoutRequest.getLines());

		List<ItemPricingData> itemPricingDataList = items.stream()
				.map(item -> {
					BigDecimal requestedQuantity =
							quantityByItemNumber.get(item.getNumber());

					return ItemPricingData.fromItemAndTotalQuantity(item, requestedQuantity);
				})
				.collect(toList());

		return new CheckoutPricingData(itemPricingDataList);
	}

	private void applyQuantityDiscounts(LocalDateTime requestTime, CheckoutPricingData checkoutPricingData) {
		Set<Long> itemIds = checkoutPricingData.getItemIds();

		List<QuantityDiscountRule> applicableRules =
				quantityPricingRuleRepository.findApplicableRules(requestTime, itemIds);

		quantityDiscountApplier.applyQuantityDiscounts(checkoutPricingData, applicableRules);
	}

	private void applyDoubleSellDiscounts(LocalDateTime requestTime, CheckoutPricingData checkoutPricingData) {
		Set<Long> itemIds = checkoutPricingData.getItemIds();

		List<DoubleSellDiscountRule> applicableRules =
				doubleSellDiscountRuleRepository.findApplicableRules(requestTime, itemIds);

		doubleSellDiscountApplier.applyDoubleSellDiscounts(checkoutPricingData, applicableRules);
	}

	private void assertHavePricesForAllItems(List<Item> items, List<LineDTO> checkoutLines) {
		Set<String> requiredItemNumbers = checkoutLines.stream()
				.map(LineDTO::getItemNumber)
				.collect(toSet());

		Set<String> foundItemNumbers = items.stream()
				.map(Item::getNumber)
				.collect(toSet());

		Set<String> missingItemNumbers =
				Sets.difference(requiredItemNumbers, foundItemNumbers);

		if (!missingItemNumbers.isEmpty()) {
			throw new CheckoutException(
					"Missing pricing information for item(s): '" +
					missingItemNumbers.stream().collect(joining("', '")) +
					"'. Please check service configuration.");
		}
	}

	private CheckoutResponseDTO createResponse(
			CheckoutRequestDTO checkoutRequest, CheckoutPricingData pricingDataList) {

		CheckoutResponseDTO responseDTO = new CheckoutResponseDTO();

		responseDTO.setRequestTime(checkoutRequest.getRequestTime());
		responseDTO.setLines(new ArrayList<>());

		for (ItemPricingData itemPricingData : pricingDataList.lines()) {
			PricedLineDTO pricedLineDTO = new PricedLineDTO();

			pricedLineDTO.setOriginalUnitPrice(itemPricingData.getOriginalUnitPrice());
			pricedLineDTO.setFinalUnitPrice(itemPricingData.getUnitPriceAfterQuantityDiscount());

			pricedLineDTO.setItemNumber(itemPricingData.getItem().getNumber());
			pricedLineDTO.setQuantity(itemPricingData.getTotalQuantity());

			responseDTO.getLines().add(pricedLineDTO);
		}

		return responseDTO;
	}
}
