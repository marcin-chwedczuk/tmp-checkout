package pl.marcinchwedczuk.checkout3.checkout.infrastructure;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public final class ValidationUtil {
	private ValidationUtil() { }

	public static ValidationErrorsDTO createUserFriendlyErrors(Errors validationErrors) {
		List<ValidationErrorsDTO.ValidationErrorDTO> errorDtoList =
			validationErrors.getAllErrors().stream()
				.map(error -> {
					if (error instanceof FieldError) {
						FieldError fieldError = (FieldError) error;
						return ValidationErrorsDTO.createErrorFromFieldNameAndMessage(
								fieldError.getField(),
								fieldError.getDefaultMessage());
					}
					else {
						return ValidationErrorsDTO.createErrorFromMessage(
								error.getDefaultMessage());
					}
				})
				.collect(Collectors.toList());

		return new ValidationErrorsDTO(errorDtoList);
	}
}
