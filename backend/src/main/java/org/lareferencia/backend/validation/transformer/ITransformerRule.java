package org.lareferencia.backend.validation.transformer;

import org.lareferencia.backend.domain.OAIRecord;

public interface ITransformerRule {
	
	
	public String getName();
	public String getDescription();
	
	abstract boolean transform(OAIRecord record);


}
