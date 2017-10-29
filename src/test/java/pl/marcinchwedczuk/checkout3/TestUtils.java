package pl.marcinchwedczuk.checkout3;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import pl.marcinchwedczuk.checkout3.infrastructure.CheckoutControllerTest;

import java.io.IOException;

public final class TestUtils {
	private TestUtils() { }

	public static String readResource(Class<?> clazz, String resourceName) {
		try {
			String resourceText = Resources.toString(
					clazz.getResource(resourceName),
					Charsets.UTF_8);

			return resourceText;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
