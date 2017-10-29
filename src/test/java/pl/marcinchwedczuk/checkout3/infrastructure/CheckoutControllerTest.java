package pl.marcinchwedczuk.checkout3.infrastructure;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.marcinchwedczuk.checkout3.Checkout3Application;
import pl.marcinchwedczuk.checkout3.development.DevelopmentService;

import java.io.IOException;
import java.nio.charset.Charset;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Checkout3Application.class)
@WebAppConfiguration
public class CheckoutControllerTest {
	private final MediaType APPLICATION_JSON_UTF8 =
			new MediaType(
					MediaType.APPLICATION_JSON.getType(),
					MediaType.APPLICATION_JSON.getSubtype(),
					Charset.forName("utf8"));

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	private DevelopmentService developmentService;

	private MockMvc mockMvc;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup(webApplicationContext).build();

		developmentService.dropCreateTestData();
	}

	@Test
	public void checkout_given_valid_data_returns_prices() throws Exception {
		mockMvc.perform(
			post("/checkout")
				.content(readResource("checkout_request_body_item_a_300_and_item_b_30.json"))
				.contentType(APPLICATION_JSON_UTF8)
		)
		.andExpect(status().isOk())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(content().json(
				readResource("checkout_response_body_item_a_300_and_item_b_30.json")));
	}

	private static String readResource(String resourceName) throws IOException {
		String resourceText = Resources.toString(
				CheckoutControllerTest.class.getResource(resourceName),
				Charsets.UTF_8);

		return resourceText;
	}
}
