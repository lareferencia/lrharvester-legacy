package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FieldValidationResult {
	
	private String fieldName;
	private boolean valid;
	private List<ContentValidationResult> contentResults;


	public FieldValidationResult() {
		contentResults = new ArrayList<ContentValidationResult>();
	}

	public FieldValidationResult(boolean valid, String fieldName,
			List<ContentValidationResult> contentResults) {
		super();
		this.valid = valid;
		this.fieldName = fieldName;
		this.contentResults = contentResults;
	}

}
