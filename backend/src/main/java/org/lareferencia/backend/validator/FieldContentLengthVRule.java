package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class FieldContentLengthVRule extends BaseContentVRule {
	
	public FieldContentLengthVRule() {
		super();
	}

	private Integer minLength = 0;
	private Integer maxLength = Integer.MAX_VALUE;

	public FieldContentLengthVRule(String fieldName, Integer min,  Integer max) {
		super(fieldName);
		this.maxLength = min;
		this.minLength = max;
	}

	@Override
	protected boolean testOccurrenceContents(String content) {
		
		if (content == null)
			return false;
		
		return (content.length() >= minLength && content.length() <= maxLength);
	}
	
}
