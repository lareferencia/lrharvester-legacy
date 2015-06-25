package org.lareferencia.backend.stats;


import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.validator.ValidationResult;

public interface IMetadataStatProcessor {
	
	public void addObservation(OAIRecordMetadata metadata, ValidationResult validationResult, Boolean wasTransformed);
	public String getIdentifier();
	public void setIdentifier(String id);
	public DataTable getStats();

}
