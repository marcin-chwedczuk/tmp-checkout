package pl.marcinchwedczuk.checkout3.checkout.application;

import java.time.LocalDateTime;
import java.util.List;

public class CheckoutResponseDTO {
	private LocalDateTime requestTime;
	private List<PricedLineDTO> lines;

	// getter / setter

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	public List<PricedLineDTO> getLines() {
		return lines;
	}

	public void setLines(List<PricedLineDTO> lines) {
		this.lines = lines;
	}
}
