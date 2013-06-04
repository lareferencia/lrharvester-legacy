package org.lareferencia.backend.indexer;

import org.lareferencia.backend.domain.NetworkSnapshot;

public interface IIndexer {
	
	
	public boolean index(NetworkSnapshot snapshot);
	
	
	//public String transform(OAIRecord record, NationalNetwork network) throws IndexerException;

}
