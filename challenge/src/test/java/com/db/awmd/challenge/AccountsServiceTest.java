package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Before;
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
import com.db.awmd.challenge.exception.InvalidFundException;
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
	
	@Autowired
	private AccountsRepository repository;


	@Mock
	private AccountsRepository acctReposObj;
	
	private static boolean fundTransfer = false;

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
	
	@Before
	public void setUp() throws Exception {

		String sourceAccountId = "1";
		String destAccountId = "2";
		Account srcAccount = accountsRepositoryInMemoryObj.getAccount(sourceAccountId);
	    Account destinationAccount = accountsRepositoryInMemoryObj.getAccount(destAccountId);
	    
	    if(Objects.isNull(srcAccount)) {
	    	accountsRepositoryInMemoryObj.createAccount(new Account(sourceAccountId, new BigDecimal("1000.00")));
	    }
	    if(Objects.isNull(destinationAccount)) {	    	
	    	accountsRepositoryInMemoryObj.createAccount(new Account(destAccountId, new BigDecimal("1001.00")));
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
		BigDecimal fund = new BigDecimal("100.0000");
		

		// when //then
		Mockito.when(acctReposObj.getAccount(srcAccountId)).thenReturn(srcAccount);
		Mockito.when(acctReposObj.getAccount(destAccountId)).thenReturn(destAccount);

		/* Multiple thread executing transfer function */
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i <= 100; i++) {

					try {
						fundTransfer	= accountsService.transferFund(srcAccountId, destAccountId, fund);
						concurrentAccounts.putIfAbsent("2", destModifiedAccount);
					} catch (AccountNotFoundException | InvalidFundException e) {
						log.error(
								"Exception Concurrency method e:"+e.getMessage());
					}

				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i <= 100; i++) {

					try {
						fundTransfer	= accountsService.transferFund(srcAccountId, destAccountId, fund);
						concurrentAccounts.putIfAbsent("2", destModifiedAccount);
					} catch (AccountNotFoundException | InvalidFundException e) {
						log.error(
								"Exception Concurrency method e:"+e.getMessage());
					}

				}
			}
		});
		t1.start();
		t2.start();
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			log.error(
					"Exception interrupted e:"+e.getMessage());
			
		}

		// assert

		assertThat(accountsRepositoryInMemoryObj.getAccount(destAccountId).getBalance())
				.isEqualTo((new BigDecimal("1901.0000")));
		

	}
	
	@Test
	public void testTransferFundNotValid() throws AccountNotFoundException {
		
		
		Map<String, Account> concurrentAccounts = new ConcurrentHashMap<>();
		 

		String srcAccountId = "1";
		String destAccountId = "2";
		Account srcAccount = new Account("1");
		Account destAccount = new Account("2");
		Account destModifiedAccount = new Account("2");
		destModifiedAccount.setBalance(new BigDecimal("1001.0000"));
		BigDecimal fund = new BigDecimal("10000.0000");

		// when //then
		Mockito.when(acctReposObj.getAccount(srcAccountId)).thenReturn(srcAccount);
		Mockito.when(acctReposObj.getAccount(destAccountId)).thenReturn(destAccount);

		/* Multiple thread executing transfer function */
		Thread t = new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i <= 3; i++) {

					try {
						fundTransfer	= accountsService.transferFund(srcAccountId, destAccountId, fund);
						concurrentAccounts.putIfAbsent("2", destModifiedAccount);
					} catch (AccountNotFoundException | InvalidFundException e) {
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
				.isEqualTo((new BigDecimal("1911.0000")));
		assertEquals(false, fundTransfer);

	}
	
	@Test
	public void testSingleThreadTransferFund() throws AccountNotFoundException, InvalidFundException {
		
		
		 

		String srcAccountId = "1";
		String destAccountId = "2";
		Account srcAccount = new Account("1");
		Account destAccount = new Account("2");
		Account destModifiedAccount = new Account("2");
		destModifiedAccount.setBalance(new BigDecimal("1001.0000"));
		BigDecimal fund = new BigDecimal("10.0000");

		// when //then
		Mockito.when(acctReposObj.getAccount(srcAccountId)).thenReturn(srcAccount);
		Mockito.when(acctReposObj.getAccount(destAccountId)).thenReturn(destAccount);


						fundTransfer	= accountsService.transferFund(srcAccountId, destAccountId, fund);
		
		// assert

		assertThat(accountsRepositoryInMemoryObj.getAccount(destAccountId).getBalance())
				.isEqualTo((new BigDecimal("1911.0000")));
		assertEquals(true, fundTransfer);

	}
	
	@Test
	public void testEmailSentNotification() throws AccountNotFoundException, InvalidFundException {

		
		String srcAccountId = "3";
		String destAccountId = "4";
		BigDecimal fund = new BigDecimal("10.0000");
		boolean emailSent= false;
		
	    accountsRepositoryInMemoryObj.createAccount(new Account(srcAccountId, new BigDecimal("1000.00")));
	    accountsRepositoryInMemoryObj.createAccount(new Account(destAccountId, new BigDecimal("1001.00")));
	   

		when(acctReposObj.getAccount(srcAccountId)).thenReturn(new Account(srcAccountId, new BigDecimal("1000.00")));
		when(acctReposObj.getAccount(destAccountId)).thenReturn(new Account(destAccountId, new BigDecimal("1001.00")));

		//if fundTransfer is true then it means email is sent to both source and destination accounts
		fundTransfer	= accountsService.transferFund(srcAccountId, destAccountId, fund);

		if(fundTransfer== true) {
			emailSent = true;
			
		}
		assertTrue(emailSent);	
	}
	
}
