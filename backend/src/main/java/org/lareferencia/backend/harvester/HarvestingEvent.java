package org.lareferencia.backend.harvester;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.lareferencia.backend.domain.NationalNetwork;
import org.lareferencia.backend.domain.OAIRecord;

@Getter
@Setter
@ToString
public class HarvestingEvent {
	
	private String errorMessage;
	private List<OAIRecord> records;
	private HarvestingEventStatus status;
	


	public HarvestingEvent(List<OAIRecord> records,
			HarvestingEventStatus ok) {
		this.records = records;
		this.status = status;
		//this.errorMessage = errorMessage;
	}


	
}
