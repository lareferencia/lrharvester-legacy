package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationResult {
	
	public ValidationResult(boolean isRecordValid) {
		valid = isRecordValid;
	}

	private boolean valid;

}
