package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DoubleSellDiscountRuleRepository extends JpaRepository<DoubleSellDiscountRule,Long> {
	@Query("select r from DoubleSellDiscountRule r " +
			"where r.item1 = :item1 " +
			"and r.item2 = :item2 " +
			"and r.validFrom <= :date " +
			"and r.validTo > :date")
	List<DoubleSellDiscountRule> findApplicableRules(
			Item item1, Item item2,
			LocalDateTime date);
}
