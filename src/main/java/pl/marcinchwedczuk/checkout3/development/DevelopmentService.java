package pl.marcinchwedczuk.checkout3.development;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.marcinchwedczuk.checkout3.checkout.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;
import static pl.marcinchwedczuk.checkout3.checkout.domain.Utils.bigDec;

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
	public void setupTestData() {
		Item itemA = new Item("item_a", new BigDecimal("100.00"));
		itemA = itemRepository.save(itemA);

		Item itemB = new Item("item_b", new BigDecimal("200.00"));
		itemB = itemRepository.save(itemB);

		QuantityPricingRule rule = new QuantityPricingRule();

		rule.setItem(itemA);
		rule.setMinQuantityInclusive(ZERO);
		rule.setMaxQuantityExclusive(bigDec(1_000_000));
		rule.setDiscount(DiscountVO.absolute(bigDec(100)));

		rule.setValidFrom(LocalDateTime.of(2010,1,1, 0,0));
		rule.setValidTo(LocalDateTime.of(2020,1,1, 0,0));

		quantityPricingRuleRepository.save(rule);
	}
}
