package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * La clase abstracta BaseVRule implementa los campos básicos que todas las reglas de validación deben cumplir
 * @author lmatas
 *
 */
public abstract class BaseVRule implements IValidationRule {
	
	public BaseVRule() {
		super();
	}

	protected String fieldName;
	protected boolean mandatory;
	

	public BaseVRule(String fieldName) {
		super();
		this.fieldName = fieldName;
	}

	
}
