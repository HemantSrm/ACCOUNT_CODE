package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;

import lombok.Getter;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

	@Autowired
	private AccountsRepository repository;

	private static Logger logger = LoggerFactory.getLogger(AccountsService.class);

	@Autowired
	private EmailNotificationService emailNotificationService;

	@Getter
	private final AccountsRepository accountsRepository;

	public AccountsRepository getAccountsRepository() {
		return accountsRepository;
	}

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

	public Account transferFund(String sourceAccountId, String destinationAccountId, BigDecimal transferAmount)
			throws AccountNotFoundException {
		
		logger.debug("Inside transferFund method");

		repository.setAccount();
		Account sourceAccount = repository.getAccount(sourceAccountId);
		Account destinationAccount = repository.getAccount(destinationAccountId);

		if ((transferAmount.intValue() < 0)
				&& (repository.getAccount(sourceAccountId).getBalance().intValue() < transferAmount.intValue()))

		{
			throw new ArithmeticException(
					"Insufficient Amount :" + repository.getAccount(sourceAccountId).getBalance());
		}

		synchronized (this) {

			logger.info("Inside synchronized block");

			try {

				if (sourceAccount.getBalance().intValue() > transferAmount.intValue()) {
					sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferAmount));
					repository.updateAccount(sourceAccount);

					destinationAccount.setBalance(destinationAccount.getBalance().add(transferAmount));
					repository.updateAccount(destinationAccount);

					 emailNotificationService.notifyAboutTransfer(destinationAccount, "fund credited : " + transferAmount);
					logger.info("email sent to destination account as fund creited :"+ transferAmount);

				} else {

					logger.info("Balance amount should not be lees than amount{} ", transferAmount);

				}

			}

			catch (Exception e) {
				logger.error("Exception e {} ", e.getMessage());
			}

		}

		return destinationAccount;
	}

}
