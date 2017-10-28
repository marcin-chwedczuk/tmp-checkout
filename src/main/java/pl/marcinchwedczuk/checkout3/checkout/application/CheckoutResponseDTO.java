package pl.marcinchwedczuk.checkout3.app;

import java.time.LocalDateTime;
import java.util.List;

public class CheckoutResponseDTO {
	private LocalDateTime checkoutTime;
	private List<PricedCheckoutLineDTO> pricedCheckoutLines;

	// getter / setter

	public LocalDateTime getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public List<PricedCheckoutLineDTO> getPricedCheckoutLines() {
		return pricedCheckoutLines;
	}

	public void setPricedCheckoutLines(List<PricedCheckoutLineDTO> pricedCheckoutLines) {
		this.pricedCheckoutLines = pricedCheckoutLines;
	}
}
