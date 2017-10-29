package pl.marcinchwedczuk.checkout3.checkout.application;

import pl.marcinchwedczuk.checkout3.checkout.domain.Item;

import java.math.BigDecimal;

public class ItemPricingData {
	public static ItemPricingData fromItemAndQuantity(Item item, BigDecimal quantity) {
		return new ItemPricingData(item, quantity);
	}

	private ItemPricingData(Item item, BigDecimal quantity) {
		this.item = item;
		this.totalQuantity = quantity;
	}

	private final Item item;
	private final BigDecimal totalQuantity;
	private BigDecimal unitPriceAfterQuantityDiscount;

	public Item getItem() {
		return item;
	}

	public BigDecimal getTotalQuantity() {
		return totalQuantity;
	}

	public BigDecimal getOriginalUnitPrice() {
		return item.getUnitPrice();
	}

	public void setUnitPriceAfterQuantityDiscount(BigDecimal unitPriceAfterQuantityDiscount) {
		this.unitPriceAfterQuantityDiscount = unitPriceAfterQuantityDiscount;
	}

	public BigDecimal getUnitPriceAfterQuantityDiscount() {
		return unitPriceAfterQuantityDiscount;
	}
}
