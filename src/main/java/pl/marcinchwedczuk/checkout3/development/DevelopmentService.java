package pl.marcinchwedczuk.checkout3.development;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marcinchwedczuk.checkout3.checkout.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;
import static pl.marcinchwedczuk.checkout3.checkout.domain.BigDecimals.bigDec;

@Service
public class DevelopmentService {
	public DevelopmentService(ItemRepository itemRepository,
							  QuantityPricingRuleRepository quantityPricingRuleRepository) {
		this.itemRepository = Objects.requireNonNull(itemRepository);
		this.quantityPricingRuleRepository = Objects.requireNonNull(quantityPricingRuleRepository);
	}

	private final ItemRepository itemRepository;
	private final QuantityPricingRuleRepository quantityPricingRuleRepository;

	@Transactional
	public void dropCreateTestData() {
		// Clean database
		quantityPricingRuleRepository.deleteAll();
		itemRepository.deleteAll();
		itemRepository.flush();

		// Create items A..F
		for (char suffix = 'A'; suffix < 'G'; suffix++) {
			Item item = new Item("ITEM_" + suffix, BigDecimals.HUNDRED);
			itemRepository.save(item);
		}

		// Create rule for ITEM_A
		{
			QuantityDiscountRule ruleA = new QuantityDiscountRule();

			Item itemA = itemRepository.findByNumber("ITEM_A").get();
			ruleA.setItem(itemA);
			ruleA.setMinQuantityInclusive(bigDec(300));
			ruleA.setMaxQuantityExclusive(bigDec(1_000_000));
			ruleA.setDiscount(DiscountVO.absolute(bigDec(10)));

			ruleA.setValidFrom(LocalDateTime.of(2010, 1, 1, 0, 0));
			ruleA.setValidTo(LocalDateTime.of(2020, 1, 1, 0, 0));

			quantityPricingRuleRepository.save(ruleA);
		}

		// Create rule for ITEM_B
		{
			QuantityDiscountRule ruleB = new QuantityDiscountRule();

			Item itemB = itemRepository.findByNumber("ITEM_B").get();
			ruleB.setItem(itemB);
			ruleB.setMinQuantityInclusive(bigDec(100));
			ruleB.setMaxQuantityExclusive(bigDec(1_000_000));
			ruleB.setDiscount(DiscountVO.percentage(bigDec(50)));

			ruleB.setValidFrom(LocalDateTime.of(2010, 1, 1, 0, 0));
			ruleB.setValidTo(LocalDateTime.of(2020, 1, 1, 0, 0));

			quantityPricingRuleRepository.save(ruleB);
		}
	}
}
