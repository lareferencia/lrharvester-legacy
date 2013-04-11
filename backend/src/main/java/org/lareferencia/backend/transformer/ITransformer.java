package org.lareferencia.backend.transformer;

import org.lareferencia.backend.harvester.HarvesterRecord;

public interface ITransformer {
	
	
	public HarvesterRecord transform(HarvesterRecord record);

}
