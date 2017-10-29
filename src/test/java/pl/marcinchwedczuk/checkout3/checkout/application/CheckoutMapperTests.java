package pl.marcinchwedczuk.checkout3.checkout.application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import pl.marcinchwedczuk.checkout3.ObjectMother;
import pl.marcinchwedczuk.checkout3.checkout.domain.Item;
import pl.marcinchwedczuk.checkout3.checkout.domain.ItemRepository;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.CheckoutPricingData;
import pl.marcinchwedczuk.checkout3.checkout.domain.pricing.ItemPricingData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals.bigDec;

public class CheckoutMapperTests {

	private Item itemA;
	private Item itemB;
	private ItemRepository itemRepository;

	private CheckoutMapper checkoutMapper;

	@Before
	public void before() {
		itemA = ObjectMother.newItemFromNameAndPrice("ITEM_A", 666);
		itemB = ObjectMother.newItemFromNameAndPrice("ITEM_B", 667);
		itemRepository = mock(ItemRepository.class);

		checkoutMapper = new CheckoutMapper(itemRepository);
	}

	@Test
	public void mapToCheckoutPricingData_works() {
		// given
		CheckoutRequestDTO requestDTO = new CheckoutRequestDTO();
		requestDTO.setRequestTime(ObjectMother.DATE_2017_01_01);

		LineDTO itemALineDTO = new LineDTO();
		itemALineDTO.setItemNumber("ITEM_A");
		itemALineDTO.setQuantity(bigDec(11));

		LineDTO itemBLineDTO = new LineDTO();
		itemBLineDTO.setItemNumber("ITEM_B");
		itemBLineDTO.setQuantity(bigDec(77));

		requestDTO.setLines(asList(itemALineDTO, itemBLineDTO));

		when(itemRepository.findAllByNumberIn(any()))
				.thenReturn(Arrays.asList(itemA, itemB));

		// when
		CheckoutPricingData checkoutPricingData =
				checkoutMapper.mapToCheckoutPricingData(requestDTO);

		// then
		assertThat(checkoutPricingData).isNotNull();

		List<ItemPricingData> lines = new ArrayList<>(checkoutPricingData.lines());
		assertThat(lines).hasSize(2);

		// check ITEM_A
		assertThat(lines.get(0).getItem())
				.as("item")
				.isSameAs(itemA);
		assertThat(lines.get(0).getTotalQuantity())
				.as("total quantity")
				.isEqualByComparingTo(bigDec(11));

		// check ITEM_B
		assertThat(lines.get(1).getItem())
				.as("item")
				.isSameAs(itemB);
		assertThat(lines.get(1).getTotalQuantity())
				.as("total quantity")
				.isEqualByComparingTo(bigDec(77));

		// check "ITEM_A" and "ITEM_B" were passed to findAllByNumberIn
		ArgumentCaptor<Collection> argument = ArgumentCaptor.forClass(Collection.class);
		verify(itemRepository).findAllByNumberIn(argument.capture());
		assertThat(argument.getValue())
				.containsExactlyInAnyOrder("ITEM_A", "ITEM_B");
	}


	@Test
	public void mapToCheckoutResponseDTO_works() {
		// given
		ItemPricingData itemAPricingData = ItemPricingData
				.fromItemAndTotalQuantity(itemA, bigDec(20));

		itemAPricingData.setUnitPriceAfterQuantityDiscount(bigDec(123));

		itemAPricingData.createDiscountSegmentFromQtyAndPrice(
				bigDec(10), bigDec(100));

		itemAPricingData.createDiscountSegmentFromQtyAndPrice(
				bigDec(10), bigDec(80));

		ItemPricingData itemBPricingData = ItemPricingData
				.fromItemAndTotalQuantity(itemB, bigDec(100));

		itemBPricingData.setUnitPriceAfterQuantityDiscount(bigDec(86));

		CheckoutPricingData checkoutPricingData = new CheckoutPricingData(
				asList(itemAPricingData, itemBPricingData));

		// when
		CheckoutResponseDTO responseDTO = checkoutMapper.mapToCheckoutResponseDTO(
				ObjectMother.DATE_2017_01_01, checkoutPricingData);

		// then
		assertThat(responseDTO)
				.isNotNull();

		assertThat(responseDTO.getRequestTime())
				.isEqualTo(ObjectMother.DATE_2017_01_01);

		assertThat(responseDTO.getLines())
				.isNotNull()
				.hasSize(3);

		// assertPricedLine:
		// itemNumber, quantity, originalUnitPrice, finalPrice
		assertPricedLine(
				responseDTO.getLines().get(0),
				"ITEM_A", bigDec(10), bigDec(666), bigDec(100));

		assertPricedLine(
				responseDTO.getLines().get(1),
				"ITEM_A", bigDec(10), bigDec(666), bigDec(80));

		assertPricedLine(
				responseDTO.getLines().get(2),
				"ITEM_B", bigDec(100), bigDec(667), bigDec(86));

	}

	private static void assertPricedLine(
			PricedLineDTO pricedLineDTO,
			String itemNumber, BigDecimal quantity,
			BigDecimal originalUnitPrice, BigDecimal finalPrice)
	{
		assertThat(pricedLineDTO.getItemNumber())
				.isEqualTo(itemNumber);

		assertThat(pricedLineDTO.getQuantity())
				.as("quantity")
				.isEqualByComparingTo(quantity);

		assertThat(pricedLineDTO.getOriginalUnitPrice())
				.as("original unit price")
				.isEqualByComparingTo(originalUnitPrice);

		assertThat(pricedLineDTO.getFinalUnitPrice())
				.as("final price")
				.isEqualByComparingTo(finalPrice);
	}
}
