package pl.marcinchwedczuk.checkout3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import pl.marcinchwedczuk.checkout3.checkout.domain.BaseEntity;
import pl.marcinchwedczuk.checkout3.checkout.infrastructure.BigDecimalSerializer;

import java.math.BigDecimal;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackageClasses = {
		BaseEntity.class,

		// Add support for Java 8 dates.
		// See: https://stackoverflow.com/a/29271707/1779504
		Jsr310JpaConverters.class
})
public class Checkout3Application {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonSerializeBigDecimalAsString(
			BigDecimalSerializer bigDecimalSerializer) {
		return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder
				.serializerByType(BigDecimal.class, bigDecimalSerializer);
	}

	public static void main(String[] args) {
		SpringApplication.run(Checkout3Application.class, args);
	}
}
