package com.hkr.account.service;

import com.hkr.account.model.Accounts;

public interface FundTransferNotificationService {

	void notifyAboutTransfer(Accounts accounts, String transferDescription);
}
