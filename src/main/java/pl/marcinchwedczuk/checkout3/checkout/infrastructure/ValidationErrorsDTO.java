package pl.marcinchwedczuk.checkout3.checkout.infrastructure;

import java.util.List;

public class ValidationErrorsDTO {
	public ValidationErrorsDTO(List<ValidationErrorDTO> validationErrors) {
		this.validationErrors = validationErrors;
	}

	private final List<ValidationErrorDTO> validationErrors;

	public List<ValidationErrorDTO> getValidationErrors() {
		return validationErrors;
	}

	public static ValidationErrorDTO createErrorFromMessage(String message) {
		return new ValidationErrorDTO(null, message);
	}

	public static ValidationErrorDTO createErrorFromFieldNameAndMessage(String fieldName, String message) {
		return new ValidationErrorDTO(fieldName, message);
	}

	public static class ValidationErrorDTO {
		private ValidationErrorDTO(String fieldName, String validationMessage) {
			this.fieldName = fieldName;
			this.validationMessage = validationMessage;
		}

		private final String fieldName;
		private final String validationMessage;

		public String getFieldName() {
			return fieldName;
		}

		public String getValidationMessage() {
			return validationMessage;
		}
	}
}
