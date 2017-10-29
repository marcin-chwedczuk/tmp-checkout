package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "quantity_discount_rule")
public class QuantityDiscountRule extends DiscountRule {
	@ManyToOne
	@JoinColumn(name = "item_id", nullable = false,
			foreignKey = @ForeignKey(name = "fk_quantity_rule_item_id"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Item item;

	@Column(name = "min_qty_inclusive", nullable = false, precision = 10)
	private BigDecimal minQuantityInclusive;

	@Column(name = "max_qty_exclusive", nullable = false, precision = 10)
	private BigDecimal maxQuantityExclusive;

	// @formatter:off
	@AttributeOverrides({
		@AttributeOverride(
			name = "discountValue",
			column = @Column(name = "discount_value", nullable = false,
					precision = MONETARY_PRECISION, scale = MONETARY_SCALE)),
		@AttributeOverride(
			name = "discountType",
			column = @Column(name = "discount_type", nullable = false, length = 16)
		)
	})
	// @formatter:on
	private DiscountVO discount;

	public boolean isApplicableToQuantity(BigDecimal quantity) {
		if (quantity.compareTo(getMinQuantityInclusive()) < 0)
			return false;

		if (quantity.compareTo(getMaxQuantityExclusive()) >= 0)
			return false;

		return true;
	}

	@Override
	public String toString() {
		return "QuantityDiscountRule{" +
				"item=" + item +
				", discount=" + discount +
				"} " + super.toString();
	}

	// getter / setter -----------------------------------

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public BigDecimal getMinQuantityInclusive() {
		return minQuantityInclusive;
	}

	public void setMinQuantityInclusive(BigDecimal minQuantityInclusive) {
		this.minQuantityInclusive = minQuantityInclusive;
	}

	public BigDecimal getMaxQuantityExclusive() {
		return maxQuantityExclusive;
	}

	public void setMaxQuantityExclusive(BigDecimal maxQuantityExclusive) {
		this.maxQuantityExclusive = maxQuantityExclusive;
	}

	public DiscountVO getDiscount() {
		return discount;
	}

	public void setDiscount(DiscountVO discount) {
		this.discount = discount;
	}

}
