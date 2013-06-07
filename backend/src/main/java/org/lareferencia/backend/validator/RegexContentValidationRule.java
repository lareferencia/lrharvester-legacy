package org.lareferencia.backend.validator;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.ToString;


@ToString(exclude={"pattern"})
public class RegexContentValidationRule extends BaseContentValidationRule {
	
	public static String RULE_ID="RegexValue";
	
	private @Getter String regexString;
	private Pattern pattern;
	
	public RegexContentValidationRule() {
		super();
	}

	public RegexContentValidationRule(String regexString) {
		super();
		this.regexString = regexString;
		this.pattern = Pattern.compile(regexString);
	}
	
	public RegexContentValidationRule(String regexString, String quantifier) {
		super();
		this.regexString = regexString;
		this.pattern = Pattern.compile(regexString);
		this.quantifier = quantifier;

	}
	
	public void setRegexString(String reString) {
		this.regexString = reString;
		this.pattern = Pattern.compile(reString);
	}

	@Override
	public ContentValidationResult validate(String content) {
	
		ContentValidationResult result = new ContentValidationResult();
		//result.setRuleID(RULE_ID);
		
		// Se recorta el diccionario si resulta muy grande, enumerando solo los primeros 255 chars
		//String expected = regexString;
		//result.setExpectedValue( expected.length() > MAX_EXPECTED_LENGTH ? expected.substring(0, MAX_EXPECTED_LENGTH) : expected ) ;
		
		result.setRuleName(this.name);

		if (content == null) {
			result.setReceivedValue("NULL");
			result.setValid(false);
		} else {
			result.setReceivedValue(content.length() > MAX_EXPECTED_LENGTH ? content.substring(0, MAX_EXPECTED_LENGTH) : content);
			result.setValid( pattern.matcher(content).matches() );
		}
			
		return result;
	}
}
