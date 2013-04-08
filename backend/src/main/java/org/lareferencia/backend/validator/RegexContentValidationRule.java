package org.lareferencia.backend.validator;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.ToString;


@ToString(exclude={"pattern"})
public class RegexContentValidationRule extends BaseContentValidationRule {
	
	private @Getter String regexString;
	private Pattern pattern;
	
	public RegexContentValidationRule() {
		super();
	}

	public RegexContentValidationRule(String regexString, boolean isMadatory) {
		super(isMadatory);
		this.regexString = regexString;
		this.pattern = Pattern.compile(regexString);
	}
	
	public void setRegexString(String reString) {
		this.regexString = reString;
		this.pattern = Pattern.compile(reString);
	}

	@Override
	public boolean validate(String content) {
		if (content == null) return false;
		return pattern.matcher(content).matches();
	}
}
