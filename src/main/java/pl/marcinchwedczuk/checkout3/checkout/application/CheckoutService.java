package pl.marcinchwedczuk.checkout3.app;

import org.springframework.stereotype.Service;

@Service
public class CheckoutService {

	public CheckoutResponseDTO computePrices(CheckoutRequestDTO checkoutRequest) throws ItemNotFoundException {
		CheckoutResponseDTO responseDTO = new CheckoutResponseDTO();
		responseDTO.setCheckoutTime(checkoutRequest.getCheckoutTime());

		throw new ItemNotFoundException("foo");

		// return responseDTO;
	}
}
