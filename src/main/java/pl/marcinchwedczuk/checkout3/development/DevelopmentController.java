package pl.marcinchwedczuk.checkout3.development;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinchwedczuk.checkout3.checkout.infrastructure.CheckoutController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/development")
public class DevelopmentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DevelopmentController.class);

	public DevelopmentController(DevelopmentService developmentService) {
		this.developmentService = developmentService;
	}

	private final DevelopmentService developmentService;

	@RequestMapping(value = "/init-database", method = POST)
	public ResponseEntity initDatabase() {
		try {
			developmentService.initDatabase();

			return ResponseEntity
					.status(HttpStatus.ACCEPTED)
					.body("OK");
		}
		catch (Exception e) {
			LOGGER.error("Cannot init database.", e);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("OPERATION FAILED: " + e.getMessage());
		}
	}
}
