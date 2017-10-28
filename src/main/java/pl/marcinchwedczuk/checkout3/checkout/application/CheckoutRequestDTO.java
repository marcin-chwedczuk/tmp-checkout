package pl.marcinchwedczuk.checkout3.app;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class CheckoutRequestDTO {
	@NotNull(message = "You must provide checkout time.")
	private LocalDateTime checkoutTime;

	@NotEmpty(message = "You must provide list of items to price.")
	@Valid
	private List<CheckoutLineDTO> checkoutLines;

	// getters / setters -------------------------------

	public LocalDateTime getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public List<CheckoutLineDTO> getCheckoutLines() {
		return checkoutLines;
	}

	public void setCheckoutLines(List<CheckoutLineDTO> checkoutLines) {
		this.checkoutLines = checkoutLines;
	}
}
