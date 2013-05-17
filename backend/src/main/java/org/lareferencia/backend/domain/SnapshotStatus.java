package org.lareferencia.backend.domain;

public enum SnapshotStatus {
	
	INITIALIZED,
	PROCESSING,
	RETRYING,
	FINISHED_ERROR,
	FINISHED_VALID,
	STOPPED
}

