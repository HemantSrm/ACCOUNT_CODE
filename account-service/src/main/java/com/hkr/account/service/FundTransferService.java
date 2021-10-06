package com.hkr.account.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkr.account.exception.TransactionNotDoneException;
import com.hkr.account.model.Accounts;
import com.hkr.account.model.Transaction;
import com.hkr.account.repository.AccountRepository;

@Service
public class FundTransferService {

	@Autowired
	AccountRepository repository;

	@Autowired
	FundTransferNotificationServiceImpl notifyService;

	private static Logger logger = LoggerFactory.getLogger(FundTransferService.class);

	public Transaction transferFund(Transaction transaction) throws TransactionNotDoneException {

		Long accountFromId = 0L;
		Long accountToId = 0L;
		Double amount = 0.0;

		if (transaction.getAccountList().size() == 2) {
			accountFromId = transaction.getAccountList().get(0).getAccId();
			accountToId = transaction.getAccountList().get(1).getAccId();
			amount = transaction.getAmount();
			logger.info("Deposit ammoungt {}", amount);
			if (amount < 0) // Deposit value is less than 0
			{
				throw new ArithmeticException("Deposit amount should not be negative" + amount);
			}
		}

		Optional<Accounts> fromAcc = repository.findById(accountFromId);

		Optional<Accounts> toAcc = repository.findById(accountToId);

		synchronized (this) {

			logger.info("Inside synchronized block");

			try {
				if (fromAcc.isPresent() && toAcc.isPresent()) {
					Accounts from = fromAcc.get();
					Accounts to = toAcc.get();

					if (from.getBalance() > amount) {
						from.setBalance(from.getBalance() - amount);
						to.setBalance(to.getBalance() + amount);
					} else {

						logger.info("Balance amount should not be lees than amount{} ", amount);

					}

					repository.save(from);
					repository.save(to);

					List<Accounts> accounts = new ArrayList<>();

					accounts.add(from);
					accounts.add(to);

					transaction.setAccountList(accounts);
					transaction.setAmount(amount);
					notifyService.notifyAboutTransfer(from, "fund deducted" + amount);
					logger.info("amount deducted{} ", amount);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return transaction;
	}

}
