package org.lareferencia.backend.harvester;

import lombok.Getter;
import lombok.Setter;

import org.w3c.dom.Node;


@Getter
@Setter
public class HarvesterRecord implements IHarvesterRecord {
	
	private Node domNode;
	private String identifier;

	public HarvesterRecord() {
		super();
	}

	public HarvesterRecord(String identifier, Node domNode) {
		super();
		this.domNode = domNode;
		this.identifier = identifier;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}




}
