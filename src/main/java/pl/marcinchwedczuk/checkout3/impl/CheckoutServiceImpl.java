package pl.marcinchwedczuk.checkout3;

import org.springframework.stereotype.Service;
import pl.marcinchwedczuk.checkout3.api.CheckoutRequestDTO;
import pl.marcinchwedczuk.checkout3.api.CheckoutResponseDTO;
import pl.marcinchwedczuk.checkout3.api.CheckoutService;

@Service
public class CheckoutServiceImpl implements CheckoutService {

	@Override
	public CheckoutResponseDTO computePrices(CheckoutRequestDTO checkoutRequest) {
		return null;
	}
}
