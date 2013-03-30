package org.lareferencia.backend.harvester;

import org.lareferencia.backend.domain.NationalNetwork;

public interface IHarvester extends IHarvestingEventSource {
	
	public void harvest(String uri, String from, String until, String setname, String metadataPrefix);
	
	
}
