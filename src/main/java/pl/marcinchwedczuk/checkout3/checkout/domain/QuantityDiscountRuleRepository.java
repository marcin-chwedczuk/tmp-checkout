package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface QuantityDiscountRuleRepository extends JpaRepository<QuantityDiscountRule,Long> {
	@Query("select r from QuantityDiscountRule r " +
			"where r.item.id in :itemIds " +
			"and (r.validFrom is null or r.validFrom <= :date) " +
			"and (r.validTo is null or r.validTo > :date)")
	List<QuantityDiscountRule> findApplicableRules(
			LocalDateTime date, Set<Long> itemIds);
}
