package org.lareferencia.backend.validator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
/**
 * La clase abstracta BaseContentVRule implementa los mecanismos comunes para la evaluación de contenidos de distintas
 * ocurrencias de un mismo metadato.
 * @author lmatas
 *
 */
public abstract class BaseContentValidationRule implements IContentValidationRule {
	
	protected static int MAX_EXPECTED_LENGTH = 255; 
	
	protected String quantifier = IContentValidationRule.QUANTIFIER_ZERO_OR_MORE;
	protected String name = "";
	
	public BaseContentValidationRule() {
	}
	
	
	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina la valides de un string
	 * @param item
	 * @return
	 */
	public abstract ContentValidationResult validate(String content);

}
