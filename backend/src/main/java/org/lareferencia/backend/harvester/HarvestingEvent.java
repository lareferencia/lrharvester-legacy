package org.lareferencia.backend.harvester;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.lareferencia.backend.domain.NationalNetwork;

@Getter
@Setter
@ToString
public class HarvestingEvent {
	
	private String errorMessage;
	private String response;
	private HarvestingResult status;
	
	public HarvestingEvent(String response, HarvestingResult status) {
		this.response = response;
		this.status = status;
	}
	
	public HarvestingEvent(String response, String errorMsg, HarvestingResult status) {
		this.response = response;
		this.status = status;
		this.errorMessage = errorMessage;
		
	}


	
}
