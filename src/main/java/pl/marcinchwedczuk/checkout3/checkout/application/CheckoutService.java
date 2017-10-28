package pl.marcinchwedczuk.checkout3.checkout.application;

import org.springframework.stereotype.Service;
import pl.marcinchwedczuk.checkout3.checkout.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.function.Function.identity;
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

	public CheckoutResponseDTO computePrices(CheckoutRequestDTO checkoutRequest) throws ItemNotFoundException {

		Map<String, BigDecimal> quantityByItemNumber = checkoutRequest.getCheckoutLines().stream()
				.collect(groupingBy(CheckoutLineDTO::getItemNumber,
						mapping(CheckoutLineDTO::getQuantity, summingBigDecimal())));

		List<Item> items = itemRepository.findAllByNumbers(quantityByItemNumber.keySet());

		List<ItemPricingData> itemPricingDataList = items.stream()
				.map(item -> ItemPricingData.fromItemAndQuantity(
								item, quantityByItemNumber.get(item.getNumber())))
				.collect(toList());

		// 1. Apply quantity rules
		LocalDateTime checkoutTime = checkoutRequest.getCheckoutTime();

		for (ItemPricingData pricingData : itemPricingDataList) {
			applyQuantityDiscount(checkoutTime, pricingData);
		}


		return null;
	}

	private void applyQuantityDiscount(LocalDateTime checkoutTime, ItemPricingData pricingData) {
		List<QuantityPricingRule> applicableRules =
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
			QuantityPricingRule rule = applicableRules.get(0);

			BigDecimal discountedPrice = pricingCalculator.computeDiscountedPrice(
					pricingData.getOriginalUnitPrice(), rule);

			pricingData.setDiscountedPrice(discountedPrice);
		}
	}
}
