package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ValidationResult {
	
	private boolean valid;
	private Map<String,FieldValidationResult> fieldResults;
	
	public ValidationResult() {
		fieldResults = new HashMap<String,FieldValidationResult>();
	}

}
