package org.lareferencia.backend.transformer;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import lombok.Getter;
import lombok.Setter;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.validator.ContentValidationResult;
import org.lareferencia.backend.validator.FieldValidationResult;
import org.w3c.dom.Node;

public class TranslateContentFieldTransformer extends FieldTransformer {
	

	@Getter
	Map<String,String> translationMap;
	
   
	@Override
	void transform(OAIRecordMetadata metadata) {
		
		ContentValidationResult result;
		boolean found = false;
		
		// Ciclo de búsqueda
		for (Node node: metadata.getFieldNodes(fieldName) ) {
			
			String occr = node.getFirstChild().getNodeValue();
			
			if (!found) {
				result = validationRule.validate(occr);
				found |= result.isValid();
			}
		 }
		
		
		// ciclo de reemplazo
		if ( !found )
			for (Node node: metadata.getFieldNodes( this.getFieldName() ) ) {
				
				String occr = node.getFirstChild().getNodeValue();
				
				if (!found && translationMap.containsKey(occr) ) {
					node.getFirstChild().setNodeValue( translationMap.get(occr) );
					found = true;
				}	
			}
		
		// creacion del campo con el valor por defecto en caso de no haber sido encontrado
		if ( !found && this.getDefaultFieldValue() != null ) 
			metadata.addFieldOcurrence(fieldName, defaultFieldValue);
	}
	
	
	public void setTranslationMap(Map<String, String> translationMap) {
		this.translationMap = new TreeMap<String, String>(CaseInsensitiveComparator.INSTANCE);
		this.translationMap.putAll(translationMap);
	}


	static class CaseInsensitiveComparator implements Comparator<String> {
	    public static final CaseInsensitiveComparator INSTANCE = 
	           new CaseInsensitiveComparator();

	    public int compare(String first, String second) {
	         // some null checks
	         return first.compareToIgnoreCase(second);
	    }
	}

}
