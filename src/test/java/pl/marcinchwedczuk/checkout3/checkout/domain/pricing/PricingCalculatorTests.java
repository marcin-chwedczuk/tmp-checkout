package pl.marcinchwedczuk.checkout3.checkout.domain.pricing;

import org.junit.Before;
import org.junit.Test;
import pl.marcinchwedczuk.checkout3.ObjectMother;
import pl.marcinchwedczuk.checkout3.checkout.domain.DiscountVO;
import pl.marcinchwedczuk.checkout3.checkout.domain.DoubleSellDiscountRule;
import pl.marcinchwedczuk.checkout3.checkout.domain.Item;
import pl.marcinchwedczuk.checkout3.checkout.domain.QuantityDiscountRule;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.marcinchwedczuk.checkout3.checkout.utils.BigDecimals.bigDec;

public class PricingCalculatorTests {

	private PricingCalculator pricingCalculator;

	private Item itemA;
	private Item itemB;

	@Before
	public void before() {
		this.pricingCalculator = new PricingCalculator();

		this.itemA = ObjectMother.newItemFromNameAndPrice("ITEM_A", 100);
		this.itemB = ObjectMother.newItemFromNameAndPrice("ITEM_B", 100);
	}

	@Test
	public void computes_price_for_quantity_discount_rule_with_absolute_discount() {
		// given
		QuantityDiscountRule quantityDiscountRule =
				ObjectMother.newQuantityDiscountRuleFromItemAndDiscount(
						itemA, DiscountVO.absolute(bigDec(10)));

		BigDecimal priceBeforeDiscount = bigDec(100);

		// when
		BigDecimal priceAfterDiscount =
				pricingCalculator.computeDiscountedPrice(
						priceBeforeDiscount, quantityDiscountRule);

		// then
		assertThat(priceAfterDiscount)
				.isEqualByComparingTo(bigDec(90));
	}

	@Test
	public void computes_price_for_quantity_discount_rule_with_percentage_discount() {
		// given
		QuantityDiscountRule quantityDiscountRule =
				ObjectMother.newQuantityDiscountRuleFromItemAndDiscount(
						itemA, DiscountVO.percentage(bigDec(50)));

		BigDecimal priceBeforeDiscount = bigDec(400);

		// when
		BigDecimal priceAfterDiscount =
				pricingCalculator.computeDiscountedPrice(
						priceBeforeDiscount, quantityDiscountRule);

		// then
		assertThat(priceAfterDiscount)
				.isEqualByComparingTo(bigDec(200));
	}

	@Test
	public void computes_price_for_double_sell_discount_rule() {
		// given
		DoubleSellDiscountRule rule =
				ObjectMother.newDoubleSellDiscountRule(
						itemA, DiscountVO.absolute(bigDec(10)),
						itemB, DiscountVO.percentage(bigDec(50)));

		BigDecimal priceItemA = bigDec(100);
		BigDecimal priceItemB = bigDec(100);

		// when
		BigDecimal itemAPriceAfterDiscount =
				pricingCalculator.computeDiscountedPriceForItem(
						itemA, priceItemA, rule);

		BigDecimal itemBPriceAfterDiscount =
				pricingCalculator.computeDiscountedPriceForItem(
						itemB, priceItemB, rule);

		// then
		assertThat(itemAPriceAfterDiscount)
				.isEqualByComparingTo(bigDec(90));

		assertThat(itemBPriceAfterDiscount)
				.isEqualByComparingTo(bigDec(50));
	}
}
