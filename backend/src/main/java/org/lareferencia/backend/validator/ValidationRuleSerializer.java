package org.lareferencia.backend.validator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;


public class ValidationRuleSerializer {

	@Getter
	private List<IValidatorRule> prototypes;
	
	// JsonObject Mapper
	private ObjectMapper mapper;
	
	public ValidationRuleSerializer() {
		prototypes = new ArrayList<IValidatorRule>();
	}
	
	@SuppressWarnings("unchecked")
	/***
	 * Esta metodo carga en el mapper creado los subtipos de la clase AbstractValidatorRule 
	 * desde los prototipos provisto por la lista, esto permite serializar y reconocer 
	 * los objetos desde/hacia JSON
	 */
	private void updateObjectMapper() {
		mapper = new ObjectMapper();
	    
		// Set con las clases de los prototipos declarados
		Set<Class<? extends AbstractValidatorRule>> aValidationRuleSubTypes = new HashSet<Class<? extends AbstractValidatorRule>>(); 	    
				
		for (IValidatorRule rule : prototypes) {
			// TODO: Ojo que esto puede ser problematico si algun de las reglas no es derivada de AbstractValidationRule
			aValidationRuleSubTypes.add((Class<? extends AbstractValidatorRule>) rule.getClass());
		}
		
		mapper.registerSubtypes(aValidationRuleSubTypes.toArray(new Class<?>[aValidationRuleSubTypes.size()]));	      
	}

	
	public void setPrototypes(List<IValidatorRule> prototypes) {
		this.prototypes = prototypes;
		
		// Cada vez que la lista de prototipos cambia hay que reconstruir el mapper
		updateObjectMapper();
	}
	
	public String serializeToJsonString(IValidatorRule rule) {
		
		 try {
			return mapper.writeValueAsString(rule);
		} catch (JsonProcessingException e) {
			// TODO Serialize rule exceptions
			e.printStackTrace();
		}
		return null;
	}
	
	public IValidatorRule deserializeFromJsonString(String jsonString) {
		
		
			try {
				return mapper.readValue(jsonString, AbstractValidatorRule.class);
			} catch (JsonParseException e) {
				
				e.printStackTrace();
			} catch (JsonMappingException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	
		return null;
	}
	
	
	

}
