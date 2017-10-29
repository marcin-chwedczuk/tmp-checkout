package pl.marcinchwedczuk.checkout3.checkout.application;

import org.springframework.stereotype.Service;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.ItemPricingData;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapperService {

	public CheckoutResponseDTO mapToCheckoutResponse(
			CheckoutRequestDTO checkoutRequest, List<ItemPricingData> pricingDataList) {

		CheckoutResponseDTO responseDTO = new CheckoutResponseDTO();

		responseDTO.setRequestTime(checkoutRequest.getRequestTime());
		responseDTO.setLines(new ArrayList<>());

		for (ItemPricingData pricingData : pricingDataList) {
			PricedLineDTO pricedLineDTO = new PricedLineDTO();

			pricedLineDTO.setOriginalUnitPrice(pricingData.getOriginalUnitPrice());
			pricedLineDTO.setFinalUnitPrice(pricingData.getUnitPriceAfterQuantityDiscount());

			pricedLineDTO.setItemNumber(pricingData.getItem().getNumber());
			pricedLineDTO.setQuantity(pricingData.getTotalQuantity());

			responseDTO.getLines().add(pricedLineDTO);
		}

		return responseDTO;
	}
}
