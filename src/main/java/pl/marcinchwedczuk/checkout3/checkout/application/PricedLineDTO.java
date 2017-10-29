package pl.marcinchwedczuk.checkout3.checkout.application;

import java.math.BigDecimal;

public class PricedLineDTO {
	private String itemNumber;
	private BigDecimal quantity;
	private BigDecimal originalUnitPrice;
	private BigDecimal finalUnitPrice;

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

	public BigDecimal getFinalUnitPrice() {
		return finalUnitPrice;
	}

	public void setFinalUnitPrice(BigDecimal finalUnitPrice) {
		this.finalUnitPrice = finalUnitPrice;
	}

	public BigDecimal getOriginalUnitPrice() {
		return originalUnitPrice;
	}

	public void setOriginalUnitPrice(BigDecimal originalUnitPrice) {
		this.originalUnitPrice = originalUnitPrice;
	}
}
