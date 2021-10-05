package com.hkr.account.repository;

import com.hkr.account.exception.DuplicateAccountIdException;
import com.hkr.account.model.Account;

public interface AccountsRepository {

 void createAccount(Account account) throws DuplicateAccountIdException;

 Account getAccount(String accountId);

 void clearAccounts();
}
