package org.lareferencia.backend.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ValidationResult {
	
	private boolean valid;
	private Map<String,FieldValidationResult> fieldResults;
	
	public ValidationResult() {
		fieldResults = new HashMap<String,FieldValidationResult>();
	}
	
	public String getValidationContentDetails() {
		
		StringBuilder sb = new StringBuilder();
		
		for ( Entry<String, FieldValidationResult> entry:fieldResults.entrySet() ) {
			
			for ( ContentValidationResult result: entry.getValue().getResults() ) {
				// Solo detalla los valores inválidos o válidos, según el caso
				if ( result.isValid() == this.isValid()  ) {
					sb.append( entry.getKey() + ":" + result.getReceivedValue() );
				}
				sb.append(";");
			}
		}
		
		if ( sb.charAt( sb.length() - 1) == ';' )
			sb.deleteCharAt( sb.length() - 1);
		
		return sb.toString();
	}
	
	@Override
	public String toString() {
		
		String toStr = "Validation: ";
		toStr += " record valid=" + valid + "\n\n";
		
		for ( Entry<String, FieldValidationResult> entry:fieldResults.entrySet() ) {
			
			toStr += entry.getKey() + ":\n";
			toStr += entry.getValue().toString() + "\n\n";
		}
		return toStr;
	}

}
