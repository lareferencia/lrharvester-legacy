package org.lareferencia.backend.harvester;

import java.util.List;

public interface IHarvester extends IHarvestingEventSource {
	
	
	public void harvest(String uri, String from, String until, String setname, String metadataPrefix, String resumptionToken, int maxRetries);
	public List<String> listSets(String uri);
	
}
