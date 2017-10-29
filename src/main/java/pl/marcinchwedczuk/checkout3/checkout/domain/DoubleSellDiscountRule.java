package pl.marcinchwedczuk.checkout3.checkout.domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "double_sell_discount_rule")
public class DoubleSellDiscountRule extends DiscountRule {
	@ManyToOne
	@JoinColumn(name = "item1_id", nullable = false,
			foreignKey = @ForeignKey(name = "fk_quantity_rule_item1_id"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Item item1;

	@ManyToOne
	@JoinColumn(name = "item2_id", nullable = false,
			foreignKey = @ForeignKey(name = "fk_quantity_rule_item2_id"))
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Item item2;

	// @formatter:off
	@AttributeOverrides({
		@AttributeOverride(
			name = "discountValue",
			column = @Column(name = "discount1_value", nullable = false,
					precision = MONETARY_PRECISION, scale = MONETARY_SCALE)),
		@AttributeOverride(
			name = "discountType",
			column = @Column(name = "discount1_type", nullable = false, length = 16)
		)
	})
	// @formatter:on
	private DiscountVO discount1;

	// @formatter:off
	@AttributeOverrides({
		@AttributeOverride(
			name = "discountValue",
			column = @Column(name = "discount2_value", nullable = false,
					precision = MONETARY_PRECISION, scale = MONETARY_SCALE)),
		@AttributeOverride(
			name = "discountType",
			column = @Column(name = "discount2_type", nullable = false, length = 16)
		)
	})
	// @formatter:on
	private DiscountVO discount2;

	// getter / setter -----------------------------------

	public Item getItem1() {
		return item1;
	}

	public void setItem1(Item item1) {
		this.item1 = item1;
	}

	public Item getItem2() {
		return item2;
	}

	public void setItem2(Item item2) {
		this.item2 = item2;
	}

	public DiscountVO getDiscount1() {
		return discount1;
	}

	public void setDiscount1(DiscountVO discount1) {
		this.discount1 = discount1;
	}

	public DiscountVO getDiscount2() {
		return discount2;
	}

	public void setDiscount2(DiscountVO discount2) {
		this.discount2 = discount2;
	}
}
