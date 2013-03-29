package org.lareferencia.backend.harvester;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseHarvestingEventSource implements IHarvestingEventSource {
	
	protected List<IHarvestingEventListener> listeners;
	

	public BaseHarvestingEventSource() {
		listeners = new LinkedList<IHarvestingEventListener>();
	}

	@Override
	public void addEventListener(IHarvestingEventListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeEventListener(IHarvestingEventListener listener) {
		listeners.remove(listener);
	}
	
	public void fireHarvestingEvent(HarvestingEvent event) {
		for (IHarvestingEventListener listener:listeners) {
			listener.harvestingEventOccurred(event);
		}
	}

}
