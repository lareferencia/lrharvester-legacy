package org.lareferencia.backend.harvester;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HarvestingEvent {
	
	private String message;
	private List<IHarvesterRecord> records;
	private HarvestingEventStatus status;
	
	public HarvestingEvent(List<IHarvesterRecord> records,
			HarvestingEventStatus status) {
		this.records = records;
		this.status = status;
	}
	
	public HarvestingEvent(String msg,
			HarvestingEventStatus status) {
		
		this.message = msg;
		this.status = status;
	}


	
}
