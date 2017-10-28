package pl.marcinchwedczuk.checkout3.api;

import java.math.BigDecimal;

public class CheckoutPositionDTO {
	private String itemNumber;
	private BigDecimal quantity;

	// getter / setter --------------------

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
}
