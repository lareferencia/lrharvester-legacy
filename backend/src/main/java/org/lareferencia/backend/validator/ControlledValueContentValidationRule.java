package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ControlledValueContentValidationRule extends BaseContentValidationRule {
	
	
	public static String RULE_ID="ControlledValue";
	
	private List<String> controlledValues;
	
	public ControlledValueContentValidationRule() {
		super();
		controlledValues = new ArrayList<String>();
	}

	public ControlledValueContentValidationRule(List<String> controlledValues) {
		super();
		this.controlledValues = controlledValues;
	}
	
	public ControlledValueContentValidationRule(List<String> controlledValues, String quantifier) {
		super();
		this.controlledValues = controlledValues;
		this.quantifier = quantifier;
	}

	@Override
	public ContentValidationResult validate(String content) {
		
		ContentValidationResult result = new ContentValidationResult();
		result.setRuleID(RULE_ID);
		
		// Se recorta el diccionario si resulta muy grande, enumerando solo los primeros 255 chars
		String expected = controlledValues.toString();
		result.setExpectedValue( expected.length() > MAX_EXPECTED_LENGTH ? expected.substring(0, MAX_EXPECTED_LENGTH) : expected ) ;
		
		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue( content.length() > MAX_EXPECTED_LENGTH ? content.substring(0, MAX_EXPECTED_LENGTH) : content);
			result.setValid( this.controlledValues.contains(content) );
		}
			
		return result;
	}


	
}
