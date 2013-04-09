package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentValidationResult {
	
	private boolean valid;
	private String ruleID;
	private String expectedValue;
	private String receivedValue;

	public ContentValidationResult() {
	}

	public ContentValidationResult(boolean valid, String ruleID,
			String expectedValue, String receivedValue) {
		super();
		this.valid = valid;
		this.ruleID = ruleID;
		this.expectedValue = expectedValue;
		this.receivedValue = receivedValue;
	}
}
