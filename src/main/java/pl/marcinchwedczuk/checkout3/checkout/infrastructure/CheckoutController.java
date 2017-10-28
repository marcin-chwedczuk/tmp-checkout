package pl.marcinchwedczuk.checkout3.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.marcinchwedczuk.checkout3.app.CheckoutRequestDTO;
import pl.marcinchwedczuk.checkout3.app.CheckoutResponseDTO;
import pl.marcinchwedczuk.checkout3.app.ItemNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class);

	public CheckoutController(CheckoutService checkoutService) {
		this.checkoutService = Objects.requireNonNull(checkoutService);
	}

	private final CheckoutService checkoutService;

	@RequestMapping(value = "/", method = POST)
	public ResponseEntity checkout(
			@RequestBody @Valid CheckoutRequestDTO checkoutRequestDTO,
			Errors validationErrors) {

		if (validationErrors.hasErrors()) {
			List<String> errors = validationErrors.getAllErrors().stream()
					.map(DefaultMessageSourceResolvable::getDefaultMessage)
					.collect(Collectors.toList());

			return ResponseEntity.badRequest()
					.body(errors);
		}

		try {
			CheckoutResponseDTO checkoutResponseDTO
					= checkoutService.computePrices(checkoutRequestDTO);

			return ResponseEntity.ok(checkoutResponseDTO);
		}
		catch (ItemNotFoundException e) {
			LOGGER.warn("Cannot price unknown item {}.", e.getItemName());

			return ResponseEntity
					.badRequest()
					.body("Cannot price unknown item '" + e.getItemName() + "'.");
		}
		catch (Exception e) {
			LOGGER.error("Unhandled exception during checkout.", e);

			return ResponseEntity
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
}