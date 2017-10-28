package pl.marcinchwedczuk.checkout3.checkout.domain;

import java.math.BigDecimal;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.UNORDERED;

public final class BigDecimals {
	private BigDecimals() { }

	public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

	public static BigDecimal bigDec(long value) {
		return BigDecimal.valueOf(value);
	}

	public static Collector<BigDecimal,?,BigDecimal> summingBigDecimal() {
		return Collector.of(
				() -> new BigDecimal[] { BigDecimal.ZERO },
				(BigDecimal[] acc, BigDecimal element) -> acc[0] = acc[0].add(element),
				(BigDecimal[] acc1, BigDecimal[] acc2) -> { acc1[0] = acc1[0].add(acc2[0]); return acc1; },
				(BigDecimal[] acc) -> acc[0],
				UNORDERED);
	}
}
