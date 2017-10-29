package pl.marcinchwedczuk.checkout3.checkout.infrastructure;

public class CheckoutErrorDTO {
	public CheckoutErrorDTO(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public final String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
}
