package org.lareferencia.backend.validator;

import javax.xml.transform.TransformerException;

import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

	// Indica si todas las ocurrencias deben validar o basta con que cualquiera lo haga
	protected boolean mandatory = false;
	
	protected Integer minValidOccurrences = 0;
	protected Integer maxValidOccurrences = Integer.MAX_VALUE;

	public BaseContentValidationRule() {
	}
	
	public BaseContentValidationRule(boolean isMandatory) {
		mandatory = isMandatory;
	}

	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina la valides de un string
	 * @param item
	 * @return
	 */
	public abstract boolean validate(String content);

}
