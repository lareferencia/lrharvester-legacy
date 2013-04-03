package org.lareferencia.backend.harvester;

import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HarvestingEvent {
	
	private String message;
	private Map<String,IHarvesterRecord> records;
	private HarvestingEventStatus status;
	
	public HarvestingEvent(Map<String,IHarvesterRecord> records,
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
