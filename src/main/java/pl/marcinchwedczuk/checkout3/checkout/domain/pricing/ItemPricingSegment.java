package pl.marcinchwedczuk.checkout3.checkout.domain.pricing;

import java.math.BigDecimal;

public class ItemPricingSegment {
	public ItemPricingSegment(BigDecimal quantity, BigDecimal discountedPrice) {
		this.quantity = quantity;
		this.discountedPrice = discountedPrice;
	}

	private final BigDecimal quantity;
	private final BigDecimal discountedPrice;

	@Override
	public String toString() {
		return "ItemPricingSegment{" +
				"quantity=" + quantity +
				", discountedPrice=" + discountedPrice +
				'}';
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}
}
