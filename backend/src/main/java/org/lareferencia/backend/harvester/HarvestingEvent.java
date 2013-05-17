package org.lareferencia.backend.harvester;

import java.util.ArrayList;
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
	private List<OAIRecordMetadata> records;
	private HarvestingEventStatus status;
	private String resumptionToken;
	private boolean recordMissing = false;
	
	public HarvestingEvent() {
		this.records = new ArrayList<OAIRecordMetadata>(100);
		
	}
	
	public HarvestingEvent(List<OAIRecordMetadata> records,
			HarvestingEventStatus status, String resumptionToken) {
		this.records = records;
		this.status = status;
		this.resumptionToken = resumptionToken;
	}
	
	public HarvestingEvent(String msg,
			HarvestingEventStatus status) {
		
		this.records = new ArrayList<OAIRecordMetadata>(0);
		this.message = msg;
		this.status = status;
	}


	
}
