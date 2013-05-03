package org.lareferencia.backend.indexer;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.OAIRecord;
import org.w3c.dom.Document;

public interface IIndexer {
	
	public Document transform(OAIRecord record, NationalNetwork network) throws IndexerException;

}
