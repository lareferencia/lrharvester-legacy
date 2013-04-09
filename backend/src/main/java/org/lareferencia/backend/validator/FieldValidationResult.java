package org.lareferencia.backend.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class FieldValidationResult {
	
	private String fieldName;
	private boolean mandatory;
	private boolean valid;
	private List<ContentValidationResult> contentResults;


	public FieldValidationResult() {
		contentResults = new ArrayList<ContentValidationResult>();
	}

	public FieldValidationResult(boolean idValid, boolean isMandatory, String fieldName,
			List<ContentValidationResult> contentResults) {
		super();
		this.valid = idValid;
		this.mandatory = isMandatory;
		this.fieldName = fieldName;
		this.contentResults = contentResults;
	}
	
	@Override
	public String toString() {
		
		String toStr = "\tfield valid=" + valid + "\tmandatory:" + mandatory + "\n";

		for ( ContentValidationResult cr: contentResults ) {
			toStr += "\t" + cr.toString() + ":\n";
		}
		
		return toStr;
	}

}
