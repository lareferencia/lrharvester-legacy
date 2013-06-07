package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldValidationResult {
	
	private String fieldName;
	private boolean mandatory;
	private boolean valid;
	private List<ContentValidationResult> results;
	//private Map<Integer,List<ContentValidationResult>> contentResultsByOccurrenceNumber;


	public FieldValidationResult() {
		results = new ArrayList<ContentValidationResult>();
	}

	public FieldValidationResult(boolean idValid, boolean isMandatory, String fieldName,
			List<ContentValidationResult> contentResults) {
		super();
		this.valid = idValid;
		this.mandatory = isMandatory;
		this.fieldName = fieldName;
		this.results = contentResults;
	}
	
	@Override
	public String toString() {
		
		String toStr = "\tfield valid=" + valid + "\tmandatory:" + mandatory + "\n";

		for ( ContentValidationResult cr: results ) {
			toStr += "\t" + cr.toString() + ":\n";
		}
		
		return toStr;
	}

}
