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
	
	private List<String> controlledValues;
	
	public ControlledValueContentValidationRule() {
		super();
		controlledValues = new ArrayList<String>();
	}

	public ControlledValueContentValidationRule(List<String> controlledValues, boolean isMadatory) {
		super(isMadatory);
		this.controlledValues = controlledValues;
	}

	@Override
	public boolean validate(String content) {
		if (content == null) return false;
		return this.controlledValues.contains(content);
	}
}
