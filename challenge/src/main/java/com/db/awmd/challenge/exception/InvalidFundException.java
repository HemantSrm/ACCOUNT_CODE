package com.db.awmd.challenge.exception;

public class InvalidFundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidFundException() {
		super();
	}

	public InvalidFundException(String message) {
		super(message);
	}

	public InvalidFundException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidFundException(Throwable cause) {
		super(cause);
	}

	protected InvalidFundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}