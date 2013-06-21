
package org.lareferencia.backend.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.hateoas.Identifiable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Getter;
import lombok.Setter;

/**
 * 
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties({"id"})
public class OAIProviderStat implements Identifiable<String>  {
	
	@Id
	@Column(nullable = false)
	private String ipAddress;
	
	@Column(nullable = false)
	private Long requestCount = 0L;
	
	public void incrementRequestCounter() {
		requestCount++;
	}

	@Override
	public String getId() {
		return ipAddress;
	}

	public OAIProviderStat(String ipAddress) {
		super();
		this.ipAddress = ipAddress;
		this.requestCount = 0L;
	}
	
	public OAIProviderStat() {
		super();
		this.ipAddress = "unknown";
		this.requestCount = 0L;
	}
}
