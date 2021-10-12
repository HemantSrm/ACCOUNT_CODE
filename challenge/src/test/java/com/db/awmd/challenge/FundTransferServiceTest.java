package com.db.awmd.challenge;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;
import com.db.awmd.challenge.service.FundTransferService;

public class FundTransferServiceTest {

	//@Autowired
	//private FundTransferService fundTransferService;

	//@InjectMock
	//private AccountsRepositoryInMemory acctReposObj;

	@Before
	public void setUp() throws Exception {

	}

	@Test
	// @DisplayName("Transfer between two accounts")
	public void testTransferAmountBetweenAccounts() throws AccountNotFoundException {
		
		AccountsRepositoryInMemory acctReposObj = Mockito.mock(AccountsRepositoryInMemory.class);
		FundTransferService fundTransferService = Mockito.mock(FundTransferService.class);
		

		String srcAccountId = "1";
		String destAccountId = "2";
		Account srcAccount = new Account("1");
		Account destAccount = new Account("2");
		Account destModifiedAccount = new Account("2");
		destModifiedAccount.setBalance(new BigDecimal("1001.0000"));
		BigDecimal fund = new BigDecimal("1001.0000");
		Account accModified = fundTransferService.transferFund(srcAccountId, destAccountId,
				fund);
        
		// when //then
		Mockito.when(acctReposObj.getAccount(srcAccountId)).thenReturn(srcAccount);
		Mockito.when(acctReposObj.getAccount(destAccountId)).thenReturn(destAccount);

		// assert
		
		assertEquals(destModifiedAccount.getBalance(), accModified);

	}

}
