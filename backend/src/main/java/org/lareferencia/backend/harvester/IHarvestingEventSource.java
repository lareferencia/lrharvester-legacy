package org.lareferencia.backend.harvester;

public interface IHarvestingEventSource {
	public void addEventListener( IHarvestingEventListener listener );
	public void removeEventListener( IHarvestingEventListener listener );
}
