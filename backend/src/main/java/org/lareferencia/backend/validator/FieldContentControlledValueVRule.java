package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.stereotype.Component;

@Getter
@Setter
@ToString
public class FieldContentControlledValueVRule extends BaseContentVRule {
	
	private List<String> controlledValues;
	
	public FieldContentControlledValueVRule() {
		super();
		controlledValues = new ArrayList<String>();
	}

	public FieldContentControlledValueVRule(String fieldName, List<String> controlledValues) {
		super(fieldName);
		this.controlledValues = controlledValues;
	}

	@Override
	protected boolean testOccurrenceContents(String content) {
		if (content == null) return false;
		return this.controlledValues.contains(content);
	}
}
