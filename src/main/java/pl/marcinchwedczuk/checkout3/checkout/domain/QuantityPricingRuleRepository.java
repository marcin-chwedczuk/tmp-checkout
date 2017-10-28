package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface QuantityPricingRuleRepository extends CrudRepository<QuantityPricingRule,Long> {
	@Query("select r from QuantityPricingRule r " +
			"where r.item = :item " +
			"and r.minQuantityInclusive >= :quantity " +
			"and r.maxQuantityExclusive <  :quantity " +
			"and r.validFrom >= :date " +
			"and r.validTo < :date")
	List<QuantityPricingRule> findApplicableRules(
			Item item,
			BigDecimal quantity,
			LocalDateTime date);
}
