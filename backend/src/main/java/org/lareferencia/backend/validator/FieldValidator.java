package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.HarvesterRecord;

@Getter
@Setter
public class FieldValidator {
	
	public static final String QUANTIFIER_ZERO_OR_MORE = "0..*";
	public static final String QUANTIFIER_ONE_OR_MORE = "1..*";
	public static final String QUANTIFIER_ONE_ONLY = "1..1";

	private String fieldName;	
	private List<QuantifiedContentRule> contentRules;
	private Integer minOccurences = 0;
	private Integer maxOccurences = Integer.MAX_VALUE;
	
	public FieldValidator(String fieldName) {
		super();
		this.contentRules = new ArrayList<FieldValidator.QuantifiedContentRule>();
		this.fieldName = fieldName;
	}

	public FieldValidator() {
		super();
		this.contentRules = new ArrayList<FieldValidator.QuantifiedContentRule>();
	}
	
	public void addRule(String quantifier, IContentValidationRule rule) {
		this.contentRules.add( new QuantifiedContentRule(quantifier, rule) );
	}

	public FieldValidationResult validate(HarvesterRecord record) {
		
		FieldValidationResult result = new FieldValidationResult();
		result.setFieldName(fieldName);
		
		// Se obtienen todas las ocurrencias de ese campo en el registro
		List<String> occurrences = record.getFieldOcurrences(fieldName);
		
		boolean isFieldValid = true;
	
		for (QuantifiedContentRule rule:contentRules) {
			
			int validOccurrencesCount = 0;
			
			for (String content:  occurrences) {	
				
				ContentValidationResult ruleResult = rule.getRule().validate(content);
				boolean validOccurence = ruleResult.isValid();
				validOccurrencesCount += validOccurence ? 1:0;
				
				result.getContentResults().add(ruleResult);
			}
			
			boolean isRuleValid; 
			
			if  ( rule.getQuantifier() == QUANTIFIER_ONE_ONLY ) 
				isRuleValid = validOccurrencesCount == 1;
			else if ( rule.getQuantifier() == QUANTIFIER_ONE_OR_MORE ) 
				isRuleValid = validOccurrencesCount >= 1;			
			else if ( rule.getQuantifier() == QUANTIFIER_ZERO_OR_MORE) 
				isRuleValid = validOccurrencesCount >= 0;
			else 
				isRuleValid = false;
				
			isFieldValid &= isRuleValid;	
		}
		
		result.setValid(isFieldValid);
		
		return result;
	}
	
	@Getter
	@Setter
	class QuantifiedContentRule {
		public QuantifiedContentRule(String quantifier,
				IContentValidationRule rule) {
			super();
			this.quantifier = quantifier;
			this.rule = rule;
		}
		private String quantifier;
		private IContentValidationRule rule;	
	}
	
}
