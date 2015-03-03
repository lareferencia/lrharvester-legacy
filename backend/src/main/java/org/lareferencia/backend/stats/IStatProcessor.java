package org.lareferencia.backend.stats;


import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.validator.ValidationResult;

public interface IStatProcessor {
	
	public void addObservation(OAIRecordMetadata metadata, ValidationResult validationResult);
	public String getId();
	public String getName();
	public DataTable getStats();

}
