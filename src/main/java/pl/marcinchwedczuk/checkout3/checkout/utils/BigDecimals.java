package pl.marcinchwedczuk.checkout3.checkout.utils;

import java.math.BigDecimal;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class BigDecimals {
	private BigDecimals() { }

	public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

	public static BigDecimal bigDec(long value) {
		return BigDecimal.valueOf(value);
	}

	public static Collector<BigDecimal,?,BigDecimal> summingBigDecimal() {
		return Collectors.reducing(BigDecimal.ZERO, BigDecimal::add);
	}
}
