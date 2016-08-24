package org.lareferencia.backend.validation.transformer;

import org.lareferencia.backend.domain.OAIRecord;

public interface ITransformerRule {

	abstract boolean transform(OAIRecord record);
}
