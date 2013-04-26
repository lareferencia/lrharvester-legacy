package org.lareferencia.backend.indexer;

public class IndexerException extends Exception {

	private static final long serialVersionUID = -5913401095836497654L;

	public IndexerException() {
		super();
	}

	public IndexerException(String message) {
		super(message);
	}

	public IndexerException(String message, Throwable cause) {
		super(message, cause);
	}

	public IndexerException(Throwable cause) {
		super(cause);
	}
}

