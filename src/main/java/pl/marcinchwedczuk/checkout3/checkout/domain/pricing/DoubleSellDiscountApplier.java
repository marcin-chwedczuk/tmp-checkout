package pl.marcinchwedczuk.checkout3.checkout.domain.pricing;

import org.springframework.stereotype.Service;
import pl.marcinchwedczuk.checkout3.checkout.domain.DoubleSellDiscountRule;
import pl.marcinchwedczuk.checkout3.checkout.domain.PricingCalculator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
public class DoubleSellDiscountApplier {

	public DoubleSellDiscountApplier(PricingCalculator pricingCalculator) {
		this.pricingCalculator = Objects.requireNonNull(pricingCalculator);
	}

	private final PricingCalculator pricingCalculator;

	public void applyDoubleSellDiscounts(
			CheckoutPricingData checkoutPricingData,
			Collection<? extends DoubleSellDiscountRule> discountRules)
	{
		List<DoubleSellDiscountRule> rulesSortedByPriority = discountRules.stream()
				.sorted(comparing(DoubleSellDiscountRule::getPriority).reversed())
				.collect(toList());

		for(DoubleSellDiscountRule rule : rulesSortedByPriority) {
			ItemPricingData item1PricingData =
					checkoutPricingData.findPricingDataForItem(rule.getItem1());

			ItemPricingData item2PricingData =
					checkoutPricingData.findPricingDataForItem(rule.getItem2());

			BigDecimal discountableQty =
					calculateDiscountableQuantity(item1PricingData, item2PricingData);

			createDiscountSegmentForItem(rule, item1PricingData, discountableQty);
			createDiscountSegmentForItem(rule, item2PricingData, discountableQty);
		}
	}

	private BigDecimal calculateDiscountableQuantity(
			ItemPricingData item1PricingData, ItemPricingData item2PricingData)
	{
		BigDecimal discountableQty1 =
				item1PricingData.computeQuantityNotCoveredByDoubleSellDiscount();

		BigDecimal discountableQty2 =
				item2PricingData.computeQuantityNotCoveredByDoubleSellDiscount();

		return discountableQty1.min(discountableQty2);
	}

	private void createDiscountSegmentForItem(
			DoubleSellDiscountRule rule, ItemPricingData itemPricingData, BigDecimal discountableQty)
	{
		BigDecimal discountedPrice = pricingCalculator.computeDiscountedPriceForItem(
				itemPricingData.getItem(),
				itemPricingData.getUnitPriceAfterQuantityDiscount(),
				rule);

		// No discount - no new segment
		if (discountedPrice.equals(itemPricingData.getUnitPriceAfterQuantityDiscount()))
			return;

		itemPricingData.createDiscountSegment(discountableQty, discountedPrice);
	}
}
