package pl.marcinchwedczuk.checkout3.checkout.application;

import java.time.LocalDateTime;
import java.util.List;

public class CheckoutResponseDTO {
	private LocalDateTime checkoutTime;
	private List<PricedLineDTO> pricedLines;

	// getter / setter

	public LocalDateTime getCheckoutTime() {
		return checkoutTime;
	}

	public void setCheckoutTime(LocalDateTime checkoutTime) {
		this.checkoutTime = checkoutTime;
	}

	public List<PricedLineDTO> getPricedLines() {
		return pricedLines;
	}

	public void setPricedLines(List<PricedLineDTO> pricedLines) {
		this.pricedLines = pricedLines;
	}
}
