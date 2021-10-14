package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsRepositoryInMemoryTest {

	@Autowired
	private AccountsRepository acctReposObj;
	
	@Autowired
	private AccountsRepositoryInMemory acctReposObjInMemory;
	
	@Before
	public void setUp() throws Exception {
		
		acctReposObjInMemory.setAccount();
	}

	@Test
	public void testCreate_Account() {
	
		Account srcAccount = new Account("5");

		acctReposObjInMemory.createAccount(srcAccount);
		assertNotNull(acctReposObjInMemory.getAccount("5"));
	}

	@Test
	public void getAccountId() {
		String sourceAccountId = "1";
		
		Account accountObj = acctReposObjInMemory.getAccount(sourceAccountId);;
		assertNotNull(accountObj);
	}

	@Test
	public void updateAccountntBalance() {
		String sourceAccountId = "1";
		Account srcAccount = new Account(sourceAccountId,new BigDecimal("1000.0000"));
		acctReposObjInMemory.updateAccount(srcAccount);
	    assertThat(acctReposObjInMemory.getAccount(sourceAccountId).getBalance()).isEqualTo(new BigDecimal("1000.0000"));
	}

}