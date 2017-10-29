package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.springframework.stereotype.Service;
import pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals;

import java.math.BigDecimal;

import static java.math.BigDecimal.ONE;

@Service
public class PricingCalculator {
	public BigDecimal computeDiscountedPrice(
			BigDecimal priceBeforeDiscount, QuantityDiscountRule discountRule) {
		return computeDiscountedPrice(
				priceBeforeDiscount, discountRule.getDiscount(), discountRule);
	}

	public BigDecimal computeDiscountedPriceForItem(
			Item item, BigDecimal priceBeforeDiscount, DoubleSellDiscountRule discountRule)
	{
		DiscountVO discount = discountRule.getDiscountForItem(item);

		return computeDiscountedPrice(
				priceBeforeDiscount, discount, discountRule);
	}

	private BigDecimal computeDiscountedPrice(
			BigDecimal priceBeforeDiscount, DiscountVO discount, DiscountRule discountRule)
	{
		BigDecimal discountedPrice =
				computeDiscountedPrice(priceBeforeDiscount, discount);

		assertPriceNotNegativeOrZero(priceBeforeDiscount, discountedPrice, discountRule);

		return discountedPrice;
	}

	private BigDecimal computeDiscountedPrice(BigDecimal priceBeforeDiscount, DiscountVO discount) {
		BigDecimal discountedPrice;

		switch (discount.getDiscountType()) {
			case ABSOLUTE:
				discountedPrice = priceBeforeDiscount.subtract(discount.getDiscountValue());
				break;

			case PERCENTAGE:
				BigDecimal scaledPercent = discount.getDiscountValue().divide(
						BigDecimals.HUNDRED, BaseEntity.MONETARY_SCALE, BigDecimal.ROUND_CEILING);

				BigDecimal factor = ONE.subtract(scaledPercent);

				discountedPrice = priceBeforeDiscount.multiply(factor);
				break;

			default:
				throw new IllegalArgumentException("Not supported discount type: " + discount.getDiscountType());
		}

		discountedPrice = discountedPrice.setScale(BaseEntity.MONETARY_SCALE, BigDecimal.ROUND_CEILING);
		return discountedPrice;
	}

	private void assertPriceNotNegativeOrZero(BigDecimal price, BigDecimal discountedPrice, DiscountRule rule) {
		if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0)
			throw new RuntimeException(
				"Invalid service configuration. After application of rule " +
				rule.toString() + " for price " + price + " price is negative or zero.");
	}
}
