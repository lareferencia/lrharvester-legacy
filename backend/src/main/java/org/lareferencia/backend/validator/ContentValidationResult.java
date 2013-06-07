package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContentValidationResult {
	
	private boolean valid;
	private String ruleName;
	//private String expectedValue;
	private String receivedValue;

	public ContentValidationResult() {
	}

	public ContentValidationResult(boolean valid, String name,
			/*String expectedValue*/ String receivedValue) {
		super();
		this.valid = valid;
		this.ruleName = name;
		//this.expectedValue = expectedValue;
		this.receivedValue = receivedValue;
	}
}
