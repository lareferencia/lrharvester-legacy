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
public abstract class BaseContentVRule extends BaseVRule {

	// Indica si todas las ocurrencias deben validar o basta con que cualquiera lo haga
	private boolean allOccurrencesMustBeValid = false;

	public BaseContentVRule() {
		super();
	}

	public BaseContentVRule(String fieldName) {
		super(fieldName);
	}
	
	/**
	 * La función validate para BaseContentVRule implementa el esquema común de evaluación de contenidos de campos
	 * Las clases derivadas determinan la función de evaluación testOccurrenceNode
	 */
	@Override
	public final boolean validate(HarvesterRecord record) {	
		try {
			
			int length;
			boolean isOccurrenceValid;
			boolean isRuleValid;
			
			// El valor inicial depende de si la regla es válida <=> todas las ocurrencias son validas o si solo una lo es
			isRuleValid = this.isAllOccurrencesMustBeValid();
			
			
			NodeList nodes = MedatadaDOMHelper.getNodeList(record.getDomNode(), "//" + fieldName);

			for (int i=0; i<nodes.getLength(); i++) {
				
				isOccurrenceValid = this.testOccurrenceContents(nodes.item(i).getFirstChild().getNodeValue());
				
				// Si todas deben ser válidas debe ser AND y si solo alguna OR
				if (this.isAllOccurrencesMustBeValid())
					isRuleValid &= isOccurrenceValid;
				else
					isRuleValid |= isOccurrenceValid;
				
			}
					
			return isRuleValid;

		} catch (TransformerException e) {
			return false;
		}
	}

	/** 
	 *  Esta función abstracta será implementada en las derivadas y determina cuando los contenidos
	 *  de un nodo son válidos.
	 * @param item
	 * @return
	 */
	protected abstract boolean testOccurrenceContents(String content);

}
