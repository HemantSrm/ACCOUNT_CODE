package com.db.awmd.challenge.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public Account getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	@Override
	public void updateAccount(Account account) throws DuplicateAccountIdException {
		Account accountToUpdate = accounts.putIfAbsent(account.getAccountId(), account);
		if (accountToUpdate != null) {
			accountToUpdate.setBalance(account.getBalance());
		}
	}

	@Override
	public void setAccount() {
		Account acct1 = new Account("1");
		acct1.setBalance(new BigDecimal("1000.0000"));
		Account acct2 = new Account("2");
		acct2.setBalance(new BigDecimal("1001.0000"));

		Account acct3 = new Account("3");
		acct3.setBalance(new BigDecimal("1001.0000"));
		Account acct4 = new Account("4");
		acct4.setBalance(new BigDecimal("1001.0000"));

		accounts.put(acct1.getAccountId(), acct1);
		accounts.put(acct2.getAccountId(), acct2);
		accounts.put(acct3.getAccountId(), acct3);
		accounts.put(acct4.getAccountId(), acct4);

	}

}
