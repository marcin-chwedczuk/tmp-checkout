package pl.marcinchwedczuk.checkout3.development;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/development")
public class DevelopmentController {

	public DevelopmentController(DevelopmentService developmentService) {
		this.developmentService = developmentService;
	}

	private final DevelopmentService developmentService;

	@RequestMapping("/createtestdata")
	public ResponseEntity createTestData() {
		try {
			developmentService.setupTestData();
			return ResponseEntity.status(HttpStatus.ACCEPTED).build();
		}
		catch (Exception e) {
			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(e.getMessage());
		}
	}
}
