package com.db.awmd.challenge.web;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AccountDTO;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidFundException;
import com.db.awmd.challenge.service.AccountsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

	private final AccountsService accountsService;

	private final Logger log = LoggerFactory.getLogger(AccountsController.class);

	@Autowired
	public AccountsController(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	 

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
		log.info("Creating account {}", account);

		try {
			this.accountsService.createAccount(account);
		} catch (DuplicateAccountIdException daie) {
			return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@GetMapping(path = "/{accountId}")
	public Account getAccount(@PathVariable String accountId) {
		log.info("Retrieving account for id {}", accountId);
		return this.accountsService.getAccount(accountId);
	}

	@PostMapping(path = "/transfer-fund", consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Object> transferFundBetweenAccounts(@RequestBody @Valid AccountDTO accountDto)
			throws AccountNotFoundException, InvalidFundException {
		log.info("Transactions triggered ");
		boolean transferStatus = false;  

		log.info("Inside transferAmountBetweenAccounts function");
		// amount should not be negative
		if (accountDto.getTransferfund().intValue() < 0) // deposit value is negative
		{
			throw new InvalidFundException("Invalid Amount to Transfer :" + accountDto.getTransferfund());
		}
		
		if (accountDto.getSrcAccId()== null || accountDto.getDestAcctId()== null)
		{
			throw new AccountNotFoundException("Account Id not found");
		}
			transferStatus = accountsService.transferFund(accountDto.getSrcAccId(), accountDto.getDestAcctId(), accountDto.getTransferfund());
			log.info("transferStatus Account :  "+accountDto.getTransferfund());
			if (transferStatus == false) {
				
				return new ResponseEntity<>("Fund Transfer Failed", HttpStatus.BAD_REQUEST);

			} else {
				
				return new ResponseEntity<>("Fund Transfer Success", HttpStatus.OK);
				
			}

		
	}
}
