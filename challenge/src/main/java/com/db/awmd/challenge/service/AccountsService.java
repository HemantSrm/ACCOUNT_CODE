package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.InvalidFundException;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;

@Service
public class AccountsService {

	@Autowired
	private AccountsRepository repository;

	private static Logger logger = LoggerFactory.getLogger(AccountsService.class);

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Getter
	private final AccountsRepository accountsRepository;
	
	


	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}

	public boolean  transferFund(String sourceAccountId, String destinationAccountId, BigDecimal transferAmount)
			throws AccountNotFoundException, InvalidFundException {
		
		logger.debug("Inside transferFund method");
		

		Account sourceAccount = repository.getAccount(sourceAccountId);
		Account destinationAccount = repository.getAccount(destinationAccountId);
		boolean transferFund = false;

		synchronized (this) {

			logger.info("Inside synchronized block");

			try {

				if  ((sourceAccount.getBalance().intValue() > transferAmount.intValue())) {
					sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
					repository.updateAccount(sourceAccount);

					destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));
					repository.updateAccount(destinationAccount);
					transferFund = true;

					 emailNotificationService.notifyAboutTransfer(destinationAccount, "fund credited : " + transferAmount);
					 emailNotificationService.notifyAboutTransfer(sourceAccount, "fund debited : " + transferAmount);
					logger.info("email sent to destination and source account as fund creited and debioted :"+ transferAmount);

				} else {

					logger.info("Balance amount should not be lees than amount{} ", transferAmount);
					
					throw new InvalidFundException(
							"Insufficient Amount to Transfer :" + repository.getAccount(sourceAccountId).getBalance());

				}

			}

			catch (Exception e) {
				logger.error("Exception e {} ", e.getMessage());
			}

		}

		return transferFund;
	}

}
