package org.lareferencia.backend.transformer;

import org.lareferencia.backend.harvester.OAIRecordMetadata;

public interface ITransformer {
	
	

	public void transform(OAIRecordMetadata metadata);

}
