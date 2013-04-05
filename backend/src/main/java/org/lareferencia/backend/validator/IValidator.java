package org.lareferencia.backend.validator;

import org.lareferencia.backend.harvester.HarvesterRecord;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


public interface IValidator {
	
	public ValidationResult validate(HarvesterRecord record);

}
