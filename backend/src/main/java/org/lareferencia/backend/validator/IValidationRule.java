package org.lareferencia.backend.validator;

import org.lareferencia.backend.harvester.HarvesterRecord;

public interface IValidationRule {
	
	public boolean validate(HarvesterRecord record); 

}
