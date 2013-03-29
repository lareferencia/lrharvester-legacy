package org.lareferencia.backend.harvester;

import org.lareferencia.backend.domain.NationalNetwork;

public interface IHarvester extends IHarvestingEventSource {
	
	public void harvest( NationalNetwork network );
	
	
}
