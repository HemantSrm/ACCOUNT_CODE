package com.hkr.account.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hkr.account.exception.AccountNotFoundException;
import com.hkr.account.exception.DuplicateAccountIdException;
import com.hkr.account.exception.TransactionNotDoneException;
import com.hkr.account.model.Account;
import com.hkr.account.model.Transaction;
import com.hkr.account.service.AccountsService;
import com.hkr.account.service.FundTransferService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {
	
	private static Logger logger = LoggerFactory.getLogger(AccountsController.class);


 private final AccountsService accountsService;
 
 @Autowired
 FundTransferService fundTransferService;


 @Autowired
public AccountsController(AccountsService accountsService) {
this.accountsService = accountsService;
}

 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
	 logger.info("Creating account {}", account);

 try {
this.accountsService.createAccount(account);
} catch (DuplicateAccountIdException daie) {
return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
}

 return new ResponseEntity<>(HttpStatus.CREATED);
}

 @GetMapping(path = "/{accountId}")
public Account getAccount(@PathVariable String accountId) throws AccountNotFoundException {
	 logger.info("Retrieving account for id {} ", accountId);
return this.accountsService.getAccount(accountId);
}
 
 @PostMapping("/transfer-fund")
 public Transaction transferFundFromAccountToAnotherAccount(@RequestBody Transaction transaction)
		 throws TransactionNotDoneException {
	 logger.info("Transactions triggered ", transaction.toString());

     return fundTransferService.transferFund(transaction);


 }

}
