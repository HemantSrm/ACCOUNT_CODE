package com.db.awmd.challenge.exception;

public class FundNotAvailableException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FundNotAvailableException() {
		super();
	}

	public FundNotAvailableException(String message) {
		super(message);
	}

	public FundNotAvailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public FundNotAvailableException(Throwable cause) {
		super(cause);
	}

	protected FundNotAvailableException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}