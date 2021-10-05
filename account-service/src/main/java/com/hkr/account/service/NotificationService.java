package com.hkr.account.service;

import com.hkr.account.model.Account;

public interface NotificationService {

	void notifyAboutTransfer(Account account, String transferDescription);
}
