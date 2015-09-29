package org.lareferencia.backend.validation.transformer;

import org.lareferencia.backend.harvester.OAIRecordMetadata;

public interface ITransformerRule {
	
	
	
	abstract boolean transform(OAIRecordMetadata metadata);


}
