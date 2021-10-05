package com.hkr.account.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hkr.account.model.Accounts;
import com.hkr.account.model.Transaction;
import com.hkr.account.repository.AccountRepository;

@Service
public class FundTransferService {

	@Autowired
	AccountRepository repository;
	
	@Autowired
	FundTransferNotificationServiceImpl notifyService;

	public Transaction transferFund(Transaction transaction) {

		Long accountFromId = 0L;
		Long accountToId = 0L;
		Double amount = 0.0;

		if (transaction.getAccountList().size() == 2) {
			accountFromId = transaction.getAccountList().get(0).getAccId();
			accountToId = transaction.getAccountList().get(1).getAccId();
			amount = transaction.getAmount();
		}

		Optional<Accounts> fromAcc = repository.findById(accountFromId);

		Optional<Accounts> toAcc = repository.findById(accountToId);

		try {
			if (fromAcc.isPresent() && toAcc.isPresent()) {
				Accounts from = fromAcc.get();
				Accounts to = toAcc.get();

				if (from.getBalance() > amount) {
					from.setBalance(from.getBalance() - amount);
					to.setBalance(to.getBalance() + amount);
				} else {

				}

				repository.save(from);
				repository.save(to);

				List<Accounts> accounts = new ArrayList<>();

				accounts.add(from);
				accounts.add(to);

				transaction.setAccountList(accounts);
				transaction.setAmount(amount);
				notifyService.notifyAboutTransfer(from,"fund deducted"+amount);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return transaction;
	}

}
