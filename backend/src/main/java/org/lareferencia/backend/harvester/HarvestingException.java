package org.lareferencia.backend.harvester;

public class HarvestingException extends Exception {

	private static final long serialVersionUID = -5913401095836497654L;

	public HarvestingException() {
		super();
	}

	public HarvestingException(String message) {
		super(message);
	}

	public HarvestingException(String message, Throwable cause) {
		super(message, cause);
	}

	public HarvestingException(Throwable cause) {
		super(cause);
	}
}

