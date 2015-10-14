package org.lareferencia.backend.validation.transformer;

import org.lareferencia.backend.harvester.OAIRecordMetadata;

public interface ITransformerRule {
	
	
	public String getName();
	public String getDescription();
	
	abstract boolean transform(OAIRecordMetadata metadata);


}
