package org.lareferencia.backend.harvester;

import lombok.Getter;
import lombok.Setter;

import org.w3c.dom.Node;


@Getter
@Setter
public class HarvesterRecord implements IHarvesterRecord {
	
	private Node domNode;

	public HarvesterRecord() {
		super();
	}

	public HarvesterRecord(Node domNode) {
		super();
		this.domNode = domNode;
	}




}
