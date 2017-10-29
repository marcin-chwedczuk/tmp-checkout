package pl.marcinchwedczuk.checkout3.checkout.domain.pricing;

import pl.marcinchwedczuk.checkout3.checkout.domain.PricingCalculator;
import pl.marcinchwedczuk.checkout3.checkout.domain.QuantityDiscountRule;

import java.math.BigDecimal;
import java.util.Collection;

public class QuantityDiscountApplier {
	public QuantityDiscountApplier(PricingCalculator pricingCalculator) {
		this.pricingCalculator = pricingCalculator;
	}

	private final PricingCalculator pricingCalculator;

	public void applyQuantityDiscounts(
			CheckoutPricingData checkoutPricingData,
			Collection<? extends QuantityDiscountRule> discountRules)
	{

		for (QuantityDiscountRule discountRule : discountRules) {
			ItemPricingData itemPricingData =
					checkoutPricingData.findPricingDataForItem(discountRule.getItem());

			if (!discountRule.isApplicableToQuantity(itemPricingData.getTotalQuantity()))
				continue;

			assertNoQuantityDiscountWasApplied(itemPricingData);

			BigDecimal discountedPrice = pricingCalculator.computeDiscountedPrice(
					itemPricingData.getOriginalUnitPrice(), discountRule);

			itemPricingData.setUnitPriceAfterQuantityDiscount(discountedPrice);
		}

		// For items that have no discount rules
		// set price after discount to unit price.

		for (ItemPricingData itemPricingData : checkoutPricingData.lines()) {
			if (!itemPricingData.hasQuantityDiscount()) {
				itemPricingData.setUnitPriceAfterQuantityDiscount(
						itemPricingData.getOriginalUnitPrice());
			}
		}
	}

	private void assertNoQuantityDiscountWasApplied(ItemPricingData itemPricingData) {
		if (itemPricingData.hasQuantityDiscount())
			throw new RuntimeException(
					"Invalid configuration for item: " +
					itemPricingData.getItem().getNumber() + ". " +
					"Found more than one applicable quantity rule.");
	}
}
