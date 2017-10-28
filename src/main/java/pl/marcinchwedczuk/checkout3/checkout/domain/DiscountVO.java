package pl.marcinchwedczuk.checkout3.checkout.domain;

import com.sun.javaws.exceptions.InvalidArgumentException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class DiscountVO implements Serializable {
	private BigDecimal discountValue;

	@Enumerated(EnumType.STRING)
	private DiscountType discountType;

	protected DiscountVO() { }

	public DiscountVO(BigDecimal value, DiscountType type) {
		this.discountType = Objects.requireNonNull(type);
		this.discountValue = Objects.requireNonNull(value);

		if ((type == DiscountType.PERCENTAGE) && !Utils.isValidPercentageValue(value)) {
			throw new IllegalArgumentException("Invalid value for percentage discount: " + value);
		}
	}

	@Override
	public String toString() {
		return "DiscountVO{" +
				"discountValue=" + discountValue +
				", discountType=" + discountType +
				'}';
	}

	public static DiscountVO absolute(BigDecimal value) {
		return new DiscountVO(value, DiscountType.ABSOLUTE);
	}

	public static DiscountVO percentage(BigDecimal value) {
		return new DiscountVO(value, DiscountType.PERCENTAGE);
	}

	// getter / setter -------------------------

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public DiscountType getDiscountType() {
		return discountType;
	}
}
