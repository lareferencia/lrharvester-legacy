package org.lareferencia.backend.validator;

import java.util.regex.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.stereotype.Component;


@ToString(exclude={"pattern"})
public class FieldContentRegexVRule extends BaseContentVRule {
	
	private @Getter String regexString;
	private Pattern pattern;
	
	public FieldContentRegexVRule() {
		super();
	}

	public FieldContentRegexVRule(String fieldName, String regexString) {
		super(fieldName);
		this.regexString = regexString;
		this.pattern = Pattern.compile(regexString);
	}
	
	public void setRegexString(String reString) {
		this.regexString = reString;
		this.pattern = Pattern.compile(reString);
	}

	@Override
	protected boolean testOccurrenceContents(String content) {
		if (content == null) return false;
		return pattern.matcher(content).matches();
	}
}
