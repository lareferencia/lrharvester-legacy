package org.lareferencia.backend.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerException;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.HarvesterRecord;
import org.lareferencia.backend.util.MedatadaDOMHelper;
import org.w3c.dom.NodeList;

public class ValidatorImpl implements IValidator {
	
	Map<String, FieldValidator> validatorsByFieldName;
	Map<String, Boolean> isMandatoryByFieldName;
	
	
	public ValidatorImpl() {
		super();
		validatorsByFieldName = new HashMap<String, FieldValidator>();
		isMandatoryByFieldName = new HashMap<String, Boolean>();
		
	}

	public void addFieldValidator(String fieldName, FieldValidator validator, boolean isMandatory) {
		
		validator.setFieldName(fieldName);
		validatorsByFieldName.put(fieldName, validator);
		isMandatoryByFieldName.put(fieldName, isMandatory);
		
	}
	
	@Override
	public ValidationResult validate(HarvesterRecord record) {
	
		ValidationResult result = new ValidationResult();
		boolean isRecordValid = true;
		
		
		for (Entry<String, FieldValidator> entry:validatorsByFieldName.entrySet() ) {
			
			String fieldName = entry.getKey();
			
			FieldValidationResult fieldResult = entry.getValue().validate(record);
			result.getFieldResults().put( fieldName, fieldResult );
			
			isRecordValid &= ( fieldResult.isValid() || !isMandatoryByFieldName.get(fieldName) );
		}
		
		result.setValid(isRecordValid);
		
		return result;
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
