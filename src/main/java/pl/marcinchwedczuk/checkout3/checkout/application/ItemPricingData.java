package pl.marcinchwedczuk.checkout3.checkout.application;

import pl.marcinchwedczuk.checkout3.checkout.domain.BigDecimals;
import pl.marcinchwedczuk.checkout3.checkout.domain.Item;

import java.math.BigDecimal;

public class ItemPricingData {
	public static ItemPricingData fromItemAndQuantity(Item item, BigDecimal quantity) {
		return new ItemPricingData(item, quantity);
	}

	private ItemPricingData(Item item, BigDecimal quantity) {
		this.item = item;
		this.quantity = quantity;
	}

	private final Item item;
	private final BigDecimal quantity;
	private BigDecimal discountedPrice;

	public Item getItem() {
		return item;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public BigDecimal getOriginalUnitPrice() {
		return item.getUnitPrice();
	}

	public void setDiscountedPrice(BigDecimal discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public BigDecimal getDiscountedPrice() {
		return discountedPrice;
	}
}
