package org.lareferencia.backend.domain;

public enum SnapshotStatus {
	
	INITIALIZED,
	
	HARVESTING,
	TRANSFORMING,
	VALIDATING,
	INDEXING,
	
	ERROR,
	FINISHED
}

