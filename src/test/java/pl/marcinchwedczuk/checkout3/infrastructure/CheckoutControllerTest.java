package pl.marcinchwedczuk.checkout3.infrastructure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import pl.marcinchwedczuk.checkout3.Checkout3Application;
import pl.marcinchwedczuk.checkout3.TestUtils;
import pl.marcinchwedczuk.checkout3.development.DevelopmentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.marcinchwedczuk.checkout3.MediaTypes.APPLICATION_JSON_UTF8;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Checkout3Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class CheckoutControllerTest {
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

	@Test
	public void checkout_given_data_with_unknown_item_returns_4xx_status_and_list_of_unknown_items() throws Exception {
		mockMvc.perform(
			post("/checkout")
				.content(readResource("checkout_request_body_unknown_item.json"))
				.contentType(APPLICATION_JSON_UTF8)
		)
		.andExpect(status().is4xxClientError())
		.andExpect(content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(content().json(
				readResource("checkout_response_body_unknown_item.json")));
	}

	@Test
	public void checkout_given_data_with_missing_parts_returns4xx_status_and_error_message() throws Exception {
		mockMvc.perform(
				post("/checkout")
						.content(readResource("checkout_request_body_missing_lines.json"))
						.contentType(APPLICATION_JSON_UTF8)
		)
				.andExpect(status().is4xxClientError())
				.andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(content().json(
						readResource("checkout_response_body_missing_lines.json")));
	}

	private static String readResource(String resourceName) {
		return TestUtils.readResource(CheckoutControllerTest.class, resourceName);
	}
}
