package org.lareferencia.backend.harvester;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.lareferencia.backend.domain.OAIRecord;

@Getter
@Setter
@ToString
public class HarvestingEvent {
	
	private String message;
	private List<OAIRecord> records;
	private HarvestingEventStatus status;
	
	public HarvestingEvent(List<OAIRecord> records,
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
