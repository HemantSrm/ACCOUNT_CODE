package com.hkr.account.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.hkr.account.model.AccountErrorMessage;



@RestControllerAdvice
@ResponseStatus
public class RestResponseEntityTransactionExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<AccountErrorMessage> acctNotFoundException(TransactionNotDoneException accException, WebRequest request) {

		AccountErrorMessage errMessage = new AccountErrorMessage(HttpStatus.NOT_FOUND, accException.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errMessage);

	}
}
