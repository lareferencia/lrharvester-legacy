package org.lareferencia.backend.harvester;

import org.springframework.transaction.annotation.Transactional;

public interface IHarvestingEventListener {
	
	public void harvestingEventOccurred(HarvestingEvent event);

}
