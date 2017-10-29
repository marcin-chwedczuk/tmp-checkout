package pl.marcinchwedczuk.checkout3.checkout.infrastructure;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.stereotype.Component;
import pl.marcinchwedczuk.checkout3.checkout.domain.BigDecimals;

import java.io.IOException;
import java.math.BigDecimal;

@Component
public class BigDecimalSerializer extends StdSerializer<BigDecimal> {
	public BigDecimalSerializer() {
		super(BigDecimal.class);
	}

	@Override
	public void serialize(
			BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

		if (bigDecimal == null) {
			jsonGenerator.writeNull();
		}
		else {
			jsonGenerator.writeString(bigDecimal.toString());
		}
	}
}
