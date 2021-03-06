package pl.marcinchwedczuk.checkout3.checkout.application;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class LineDTO {
	@NotNull(message = "Missing required value. You must provide item number.")
	private String itemNumber;

	@NotNull(message = "Missing required value. Quantity must be provided.")
	@DecimalMin(value = "0", message = "Quantity cannot be less than zero.")
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
