package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.br.CNPJ;
import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class LengthContentValidationRule extends BaseContentValidationRule {
	
	public static String RULE_ID="Length";
	
	public LengthContentValidationRule() {
		super();
	}

	private Integer minLength = 0;
	private Integer maxLength = Integer.MAX_VALUE;

	public LengthContentValidationRule(Integer min,  Integer max) {
		super();
		this.maxLength = max;
		this.minLength = min;
	}

	@Override
	public ContentValidationResult validate(String content) {
		
		ContentValidationResult result = new ContentValidationResult();
		result.setRuleID(RULE_ID);
		
		result.setExpectedValue( minLength.toString() + " >= Length >= " + maxLength.toString()) ;
		
		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue( new Integer(content.length()).toString() );
			result.setValid( content.length() >= minLength && content.length() <= maxLength );
		}
			
		return result;
	}
	
}
