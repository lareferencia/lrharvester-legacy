package org.lareferencia.backend.stats;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lareferencia.backend.harvester.OAIRecordMetadata;
import org.lareferencia.backend.util.datatable.DataTable;
import org.lareferencia.backend.validation.validator.ValidationResult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(value="prototype")
public class MetadataStatsManagerImpl implements IMetadataStatsManager {
		
	List<IMetadataStatProcessor> metatadaStatProcesors;

	@Override
	public void addMetadataObservation(OAIRecordMetadata metadata, ValidationResult validationResult,  Boolean wasTransformed) {
		
		for (IMetadataStatProcessor processor : metatadaStatProcesors) {
			processor.addObservation(metadata, validationResult, wasTransformed);
		}

	}
	
	@Override
	public List<IMetadataStatProcessor> getMetadataStatProcessors() {
		return metatadaStatProcesors;
	}

	@Override
	public void setMetadataStatProcessors(List<IMetadataStatProcessor> procesors) {
		metatadaStatProcesors = procesors;
	}

	///////////
	
	@Override
	public Map<String, DataTable> getStats() {
		Map<String,DataTable> result = new LinkedHashMap<String,DataTable>();
		
		for (IMetadataStatProcessor processor : metatadaStatProcesors) 
			result.put(processor.getIdentifier(), processor.getStats() );
		
		return result;
	}

}
