package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {
	
	private static Logger log = LoggerFactory.getLogger(AccountsServiceTest.class);

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private AccountsRepositoryInMemory accountsRepositoryInMemoryObj;

	@Mock
	private AccountsRepository acctReposObj;

	@Test
	public void addAccount() throws Exception {
		Account account = new Account("Id-123");
		account.setBalance(new BigDecimal(1000));
		this.accountsService.createAccount(account);

		assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
	}

	@Test
	public void addAccount_failsOnDuplicateId() throws Exception {
		String uniqueId = "Id-" + System.currentTimeMillis();
		Account account = new Account(uniqueId);
		this.accountsService.createAccount(account);

		try {
			this.accountsService.createAccount(account);
			fail("Should have failed when adding duplicate account");
		} catch (DuplicateAccountIdException ex) {
			assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
		}

	}

	@Test
	public void testTransferAmountBetweenAccounts() throws AccountNotFoundException {
		

		Map<String, Account> concurrentAccounts = new ConcurrentHashMap<>();

		String srcAccountId = "1";
		String destAccountId = "2";
		Account srcAccount = new Account("1");
		Account destAccount = new Account("2");
		Account destModifiedAccount = new Account("2");
		destModifiedAccount.setBalance(new BigDecimal("1001.0000"));
		BigDecimal fund = new BigDecimal("1001.0000");
		// acctReposObj.setAccount();

		// when //then
		Mockito.when(acctReposObj.getAccount(srcAccountId)).thenReturn(srcAccount);
		Mockito.when(acctReposObj.getAccount(destAccountId)).thenReturn(destAccount);

		/* Multiple thread executing transfer function */
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i <= 3; i++) {

					try {
						Account	accModified = accountsService.transferFund(srcAccountId, destAccountId, fund);
						concurrentAccounts.putIfAbsent(accModified.getAccountId(), accModified);
					} catch (AccountNotFoundException e) {
						log.error(
								"Exception Concurrency method e:"+e.getMessage());
					}

				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			log.error(
					"Exception interrupted e:"+e.getMessage());
			
		}

		// assert

		assertThat(accountsRepositoryInMemoryObj.getAccount(destAccountId).getBalance())
				.isEqualTo((new BigDecimal("1001.0000")));
		assertThat(accountsRepositoryInMemoryObj.getAccount(destAccountId).getBalance())
				.isEqualTo(concurrentAccounts.get(destAccountId).getBalance());

	}
}