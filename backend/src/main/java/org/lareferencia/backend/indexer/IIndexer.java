package org.lareferencia.backend.indexer;

import javax.xml.transform.TransformerException;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.OAIRecord;
import org.lareferencia.backend.harvester.HarvesterRecord;
import org.w3c.dom.Document;

public interface IIndexer {
	
	public Document transform(OAIRecord record, NationalNetwork network) throws IndexerException;

}
