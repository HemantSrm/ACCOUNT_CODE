package com.hkr.account.exception;


public class TransactionNotDoneException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TransactionNotDoneException() {
		super();
	}

	public TransactionNotDoneException(String message) {
		super(message);
	}

	public TransactionNotDoneException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransactionNotDoneException(Throwable cause) {
		super(cause);
	}

	protected TransactionNotDoneException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
