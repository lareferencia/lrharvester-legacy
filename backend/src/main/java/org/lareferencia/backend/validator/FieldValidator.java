package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.HarvesterRecord;

@Getter
@Setter
public class FieldValidator {
	

	private String fieldName;
	private boolean mandatory;
	private List<IContentValidationRule> contentRules;

	public FieldValidator(String fieldName, boolean isMandatory) {
		super();
		this.contentRules = new ArrayList<IContentValidationRule>();
		this.fieldName = fieldName;
		this.mandatory = isMandatory;
	}

	public FieldValidator() {
		super();
		this.contentRules = new ArrayList<IContentValidationRule>();
	}

	public FieldValidationResult validate(HarvesterRecord record) {
		
		FieldValidationResult result = new FieldValidationResult();
		result.setFieldName(fieldName);
		result.setMandatory(mandatory ); 
		
		
		
		// Se obtienen todas las ocurrencias de ese campo en el registro
		List<String> occurrences = record.getFieldOcurrences(fieldName);
		
		boolean isFieldValid = true;
	
		for (IContentValidationRule rule:contentRules) {
			
			int validOccurrencesCount = 0;
			
			for (String content:  occurrences) {	
				
				ContentValidationResult ruleResult = rule.validate(content);
				boolean validOccurence = ruleResult.isValid();
				validOccurrencesCount += validOccurence ? 1:0;
				
				result.getContentResults().add(ruleResult);
			}
			
			boolean isRuleValid; 
			
			if  ( rule.getQuantifier().equals(IContentValidationRule.QUANTIFIER_ONE_ONLY) ) 
				isRuleValid = validOccurrencesCount == 1;
			else if ( rule.getQuantifier().equals(IContentValidationRule.QUANTIFIER_ONE_OR_MORE) ) 
				isRuleValid = validOccurrencesCount >= 1;			
			else if ( rule.getQuantifier().equals(IContentValidationRule.QUANTIFIER_ZERO_OR_MORE) ) 
				isRuleValid = validOccurrencesCount >= 0;
			else 
				isRuleValid = false;
				
			isFieldValid &= isRuleValid;	
		}
		
		result.setValid(isFieldValid);
		
		return result;
	}
	
}
