package pl.marcinchwedczuk.checkout3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pl.marcinchwedczuk.checkout3.checkout.domain.BaseEntity;

@SpringBootApplication
@EnableJpaRepositories
@EntityScan(basePackageClasses = {
		BaseEntity.class,

		// Add support for Java 8 dates.
		// See: https://stackoverflow.com/a/29271707/1779504
		Jsr310JpaConverters.class
})
public class Checkout3Application {

	public static void main(String[] args) {
		SpringApplication.run(Checkout3Application.class, args);
	}
}
