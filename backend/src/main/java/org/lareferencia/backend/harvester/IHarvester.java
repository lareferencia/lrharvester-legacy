package org.lareferencia.backend.harvester;

public interface IHarvester extends IHarvestingEventSource {
	
	public void harvest(String uri, String from, String until, String setname, String metadataPrefix, String resumptionToken);
	
	
}
