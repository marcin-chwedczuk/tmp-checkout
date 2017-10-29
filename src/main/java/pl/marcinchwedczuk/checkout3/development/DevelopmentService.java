package pl.marcinchwedczuk.checkout3.development;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marcinchwedczuk.checkout3.checkout.domain.*;
import pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals;

import java.time.LocalDateTime;
import java.util.Objects;

import static pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals.bigDec;

@Service
public class DevelopmentService {
	public DevelopmentService(ItemRepository itemRepository,
							  QuantityDiscountRuleRepository quantityDiscountRuleRepository,
							  DoubleSellDiscountRuleRepository doubleSellDiscountRuleRepository)
	{
		this.itemRepository = Objects.requireNonNull(itemRepository);
		this.quantityDiscountRuleRepository = Objects.requireNonNull(quantityDiscountRuleRepository);
		this.doubleSellDiscountRuleRepository = Objects.requireNonNull(doubleSellDiscountRuleRepository);
	}

	private final ItemRepository itemRepository;
	private final QuantityDiscountRuleRepository quantityDiscountRuleRepository;
	private final DoubleSellDiscountRuleRepository doubleSellDiscountRuleRepository;

	@Transactional
	public void dropCreateTestData() {
		dropTestData();

		createItemsAF();

		createQuantityDiscountRuleForItemA();
		createQuantityDiscountRuleForItemB();

		createDoubleSellDiscountRuleForItemsAB();
	}

	private void dropTestData() {
		quantityDiscountRuleRepository.deleteAll();
		doubleSellDiscountRuleRepository.deleteAll();
		itemRepository.deleteAll();

		itemRepository.flush();
	}

	private void createItemsAF() {
		for (char suffix = 'A'; suffix < 'G'; suffix++) {
			Item item = new Item("ITEM_" + suffix, BigDecimals.HUNDRED);
			itemRepository.save(item);
		}
	}

	private void createQuantityDiscountRuleForItemA() {
		QuantityDiscountRule quantityItemARule = new QuantityDiscountRule();

		Item itemA = itemRepository.findByNumber("ITEM_A");

		quantityItemARule.setValidFrom(LocalDateTime.of(2010, 1, 1, 0, 0));
		quantityItemARule.setValidTo(LocalDateTime.of(2020, 1, 1, 0, 0));

		quantityItemARule.setItem(itemA);
		quantityItemARule.setMinQuantityInclusive(bigDec(300));
		quantityItemARule.setMaxQuantityExclusive(bigDec(1_000_000));

		quantityItemARule.setDiscount(DiscountVO.absolute(bigDec(10)));

		quantityDiscountRuleRepository.save(quantityItemARule);
	}

	private void createQuantityDiscountRuleForItemB() {
		QuantityDiscountRule quantityItemBRule = new QuantityDiscountRule();

		Item itemB = itemRepository.findByNumber("ITEM_B");

		quantityItemBRule.setValidFrom(LocalDateTime.of(2010, 1, 1, 0, 0));
		quantityItemBRule.setValidTo(LocalDateTime.of(2020, 1, 1, 0, 0));

		quantityItemBRule.setItem(itemB);
		quantityItemBRule.setMinQuantityInclusive(bigDec(100));
		quantityItemBRule.setMaxQuantityExclusive(bigDec(1_000_000));

		quantityItemBRule.setDiscount(DiscountVO.percentage(bigDec(50)));

		quantityDiscountRuleRepository.save(quantityItemBRule);
	}

	private void createDoubleSellDiscountRuleForItemsAB() {
		DoubleSellDiscountRule doubleSellItemsABRule = new DoubleSellDiscountRule();

		doubleSellItemsABRule.setPriority(100);

		Item itemA = itemRepository.findByNumber("ITEM_A");
		Item itemB = itemRepository.findByNumber("ITEM_B");

		doubleSellItemsABRule.setItem1(itemA);
		doubleSellItemsABRule.setItem2(itemB);

		doubleSellItemsABRule.setDiscount1(DiscountVO.percentage(bigDec(5)));
		doubleSellItemsABRule.setDiscount2(DiscountVO.noDiscount());

		doubleSellDiscountRuleRepository.save(doubleSellItemsABRule);
	}
}
