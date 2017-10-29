package pl.marcinchwedczuk.checkout3;

import pl.marcinchwedczuk.checkout3.checkout.domain.DiscountVO;
import pl.marcinchwedczuk.checkout3.checkout.domain.DoubleSellDiscountRule;
import pl.marcinchwedczuk.checkout3.checkout.domain.Item;
import pl.marcinchwedczuk.checkout3.checkout.domain.QuantityDiscountRule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public final class ObjectMother {
	private ObjectMother() { }

	private static AtomicLong globalId = new AtomicLong(0);

	public static Item newItemFromNameAndPrice(String name, long price) {
		Item item = new Item(name, BigDecimal.valueOf(price));
		item.setId(globalId.getAndIncrement());

		return item;
	}

	public static QuantityDiscountRule newQuantityDiscountRuleFromItemAndDiscount(
			Item item, DiscountVO discount)
	{
		QuantityDiscountRule rule = new QuantityDiscountRule();
		rule.setId(globalId.getAndIncrement());

		rule.setValidFrom(LocalDateTime.MIN);
		rule.setValidTo(LocalDateTime.MAX);

		rule.setMinQuantityInclusive(BigDecimal.ZERO);
		rule.setMaxQuantityExclusive(BigDecimal.valueOf(Long.MAX_VALUE));

		rule.setItem(item);
		rule.setDiscount(discount);

		return rule;
	}

	public static DoubleSellDiscountRule newDoubleSellDiscountRule(
			Item item1, DiscountVO discount1,
			Item item2, DiscountVO discount2)
	{
		DoubleSellDiscountRule rule = new DoubleSellDiscountRule();
		rule.setId(globalId.getAndIncrement());

		rule.setPriority(100);

		rule.setValidFrom(LocalDateTime.MIN);
		rule.setValidTo(LocalDateTime.MAX);

		rule.setItem1(item1);
		rule.setDiscount1(discount1);

		rule.setItem2(item2);
		rule.setDiscount2(discount2);

		return rule;
	}
}
