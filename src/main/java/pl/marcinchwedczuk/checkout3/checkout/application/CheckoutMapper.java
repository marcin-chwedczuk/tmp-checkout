package pl.marcinchwedczuk.checkout3.checkout.application;

import com.google.common.collect.Sets;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.marcinchwedczuk.checkout3.checkout.domain.CheckoutException;
import pl.marcinchwedczuk.checkout3.checkout.domain.Item;
import pl.marcinchwedczuk.checkout3.checkout.domain.ItemRepository;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.CheckoutPricingData;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.ItemPricingData;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.ItemPricingSegment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;
import static pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals.summingBigDecimal;

@Service
public class CheckoutMapper {
	public CheckoutMapper(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	private final ItemRepository itemRepository;

	@Transactional(propagation = Propagation.MANDATORY)
	public CheckoutPricingData mapToCheckoutPricingData(CheckoutRequestDTO checkoutRequest) {
		Map<String, BigDecimal> quantityByItemNumber = checkoutRequest.getLines().stream()
				.collect(groupingBy(LineDTO::getItemNumber,
						mapping(LineDTO::getQuantity, summingBigDecimal())));

		List<Item> items = itemRepository.findAllByNumberIn(quantityByItemNumber.keySet());
		assertFoundAllItems(items, quantityByItemNumber.keySet());

		List<ItemPricingData> itemPricingDataList = items.stream()
				.map(item -> {
					BigDecimal totalQty =
							quantityByItemNumber.get(item.getNumber());

					return ItemPricingData.fromItemAndTotalQuantity(item, totalQty);
				})
				.collect(toList());

		return new CheckoutPricingData(itemPricingDataList);
	}

	private void assertFoundAllItems(List<Item> items, Set<String> requiredItemNumbers) {
		Set<String> foundItemNumbers = items.stream()
				.map(Item::getNumber)
				.collect(toSet());

		Set<String> missingItemNumbers =
				Sets.difference(requiredItemNumbers, foundItemNumbers);

		if (!missingItemNumbers.isEmpty()) {
			throw new CheckoutException(
					"Unknown item(s): '" +
					missingItemNumbers.stream().collect(joining("', '")) +
					"'. Please check service configuration.");
		}
	}

	public CheckoutResponseDTO mapToCheckoutResponseDTO(
			LocalDateTime requestTime, CheckoutPricingData checkoutPricingData) {

		CheckoutResponseDTO responseDTO = new CheckoutResponseDTO();

		responseDTO.setRequestTime(requestTime);
		responseDTO.setLines(new ArrayList<>());

		for (ItemPricingData itemPricingData : checkoutPricingData.lines()) {
			for (ItemPricingSegment segment : itemPricingData.asSegments()) {
				PricedLineDTO pricedLineDTO = new PricedLineDTO();

				pricedLineDTO.setFinalUnitPrice(segment.getDiscountedPrice());
				pricedLineDTO.setQuantity(segment.getQuantity());

				pricedLineDTO.setOriginalUnitPrice(itemPricingData.getOriginalUnitPrice());
				pricedLineDTO.setItemNumber(itemPricingData.getItem().getNumber());

				responseDTO.getLines().add(pricedLineDTO);
			}
		}

		return responseDTO;
	}
}
