package pl.marcinchwedczuk.checkout3.checkout.application;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class CheckoutRequestDTO {
	@NotNull(message = "Missing required value. You must provide date for which prices will be calculated.")
	private LocalDateTime requestTime;

	@NotEmpty(message = "Missing required value. You must provide list of items to price.")
	@Valid
	private List<LineDTO> lines;

	// getters / setters -------------------------------

	public LocalDateTime getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(LocalDateTime requestTime) {
		this.requestTime = requestTime;
	}

	public List<LineDTO> getLines() {
		return lines;
	}

	public void setLines(List<LineDTO> lines) {
		this.lines = lines;
	}
}
