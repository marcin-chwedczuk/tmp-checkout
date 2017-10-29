package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface DoubleSellDiscountRuleRepository extends JpaRepository<DoubleSellDiscountRule,Long> {
	@Query("select r from DoubleSellDiscountRule r " +
			"where r.item1.id in :itemIds " +
			"and r.item2.id in :itemIds " +
			"and (r.validFrom is null or r.validFrom <= :date) " +
			"and (r.validTo is null or r.validTo > :date)")
	List<DoubleSellDiscountRule> findApplicableRules(
			LocalDateTime date, Set<Long> itemIds);
}
