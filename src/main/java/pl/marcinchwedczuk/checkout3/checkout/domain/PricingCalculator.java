package pl.marcinchwedczuk.checkout3.checkout.domain;

import com.google.common.base.Preconditions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingCalculator {
	public BigDecimal computeDiscountedPrice(BigDecimal price, QuantityPricingRule rule) {
		DiscountVO discount = rule.getDiscount();
		BigDecimal discountedPrice = null;

		switch (discount.getDiscountType()) {
			case ABSOLUTE:
				discountedPrice = price.subtract(discount.getDiscountValue());
				break;

			case PERCENTAGE:
				BigDecimal factor = discount.getDiscountValue().divide(
						BigDecimals.HUNDRED, BaseEntity.MONETARY_SCALE, BigDecimal.ROUND_CEILING);

				discountedPrice = price.multiply(factor);
				break;
		}

		if (discountedPrice.compareTo(BigDecimal.ZERO) <= 0)
			throw new CheckoutException(
					"Invalid service configuration. After application of rule " +
					rule.toString() + " for price " + price + " price is negative or zero.");

		return discountedPrice;
	}

}
