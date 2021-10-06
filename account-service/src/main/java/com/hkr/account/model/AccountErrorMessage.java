package com.hkr.account.model;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class AccountErrorMessage {
	
	private HttpStatus status;
	private String message;
	
	public AccountErrorMessage(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public AccountErrorMessage(HttpStatus status) {
		super();
		this.status = status;
	}

	
	
}
