package pl.marcinchwedczuk.checkout3.checkout.domain;

import java.math.BigDecimal;

import static java.math.BigDecimal.ZERO;
import static pl.marcinchwedczuk.checkout3.checkout.domain.BigDecimals.HUNDRED;

public final class Utils {
	private Utils() { }

	public static boolean isValidPercentageValue(BigDecimal value) {
		return (value.compareTo(ZERO) >= 0) && (value.compareTo(HUNDRED) <= 0);
	}

}
