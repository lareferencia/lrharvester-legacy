package org.lareferencia.backend.validator;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.w3c.dom.NodeList;

@Getter
@Setter
public class ValidatorImpl implements IValidator {
	
	Map<String, List<IContentValidationRule>> rulesPerField;
	
	@Override
	public ValidationResult validate(HarvesterRecord record) {
	
		boolean isRecordValid = true;
		
		// Para cada campo 
		for ( String fieldName: rulesPerField.keySet() ) {
			
			boolean isFieldValid = true;
			
			// Se obtienen las reglas de ese campo
			List<IContentValidationRule> rulesList = rulesPerField.get(fieldName);
			
			// Se obtienen todas las ocurrencias de ese campo en el registro
			for (String content: record.getFieldOcurrences(fieldName) ) {
				
				boolean isOccurrenceValid = true;	
				
				// Se evaluan las reglas para esa ocurrencia
				for (IContentValidationRule rule:rulesList) {	
					boolean isRuleValid = rule.validate(content);
					isOccurrenceValid &= (isRuleValid || !rule.isMandatory());
				}
				
				isFieldValid &= isOccurrenceValid;
			}
			
			isRecordValid &= isFieldValid;
		}
		
		return new ValidationResult(isRecordValid);
	}

	/**
	 * La función validate para BaseContentVRule implementa el esquema común de evaluación de contenidos de campos
	 * Las clases derivadas determinan la función de evaluación testOccurrenceNode
	 */
	/*
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
	*/

}
