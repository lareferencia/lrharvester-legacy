package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class LengthContentValidationRule extends BaseContentValidationRule {
	
	public LengthContentValidationRule() {
		super();
	}

	private Integer minLength = 0;
	private Integer maxLength = Integer.MAX_VALUE;

	public LengthContentValidationRule(Integer min,  Integer max, boolean isMadatory) {
		super(isMadatory);
		this.maxLength = max;
		this.minLength = min;
	}

	@Override
	public boolean validate(String content) {
		
		if (content == null)
			return false;
		
		return (content.length() >= minLength && content.length() <= maxLength);
	}
	
}
