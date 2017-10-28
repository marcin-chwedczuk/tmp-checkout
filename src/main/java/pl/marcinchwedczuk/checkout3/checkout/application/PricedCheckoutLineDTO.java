package pl.marcinchwedczuk.checkout3.app;

import java.math.BigDecimal;

public class PricedCheckoutLineDTO {
	private String itemNumber;
	private BigDecimal quantity;
	private BigDecimal price;

	// getter / setter ------------------------

	public String getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
