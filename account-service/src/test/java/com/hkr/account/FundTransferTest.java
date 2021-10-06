package com.hkr.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.hkr.account.model.Accounts;
import com.hkr.account.model.Transaction;
import com.hkr.account.repository.AccountRepository;
import com.hkr.account.repository.AccountsRepository;
import com.hkr.account.service.FundTransferService;
 

 
public class FundTransferTest {
     
  
     
    
    
    
    @Autowired
    FundTransferService fundTransferService;
    
    @Mock
    AccountRepository accountsRepository;
    
    
 
   
     
    @Test
    public void transactionTest()
    {
    	
    	Transaction transaction = new Transaction();
    	transaction.setAccountList(getAccounts());
    	transaction.setAmount(20000.0);
    	
    	Transaction newtransaction = new Transaction();
    	Accounts acnt1 = new Accounts(10002L, "Ajay", "Saving", 1.0);
        Accounts acnt2 = new Accounts(10003L, "Hemant", "Saving", 40002.0);
        List<Accounts> list = new ArrayList<>();
        list.add(acnt1);
        list.add(acnt2);
        newtransaction.setAccountList(list);
    	
    	
    	
    	
    	
    	
       // when(fundTransferService.transferFund(transaction)).thenReturn(newtransaction);
         
        assertEquals(new Accounts(10002L, "Ajay", "Saving", 1.0),accountsRepository.existsById(10002L));
         
        assertEquals(2, transaction.getAccountList().size());
        //verify(dao, times(1)).getEmployeeList();
    }
     
	/*
	 * @Test public void getEmployeeByIdTest() {
	 * when(dao.getEmployeeById(1)).thenReturn(new
	 * EmployeeVO(1,"Lokesh","Gupta","user@email.com"));
	 * 
	 * EmployeeVO emp = manager.getEmployeeById(1);
	 * 
	 * assertEquals("Lokesh", emp.getFirstName()); assertEquals("Gupta",
	 * emp.getLastName()); assertEquals("user@email.com", emp.getEmail()); }
	 * 
	 * @Test public void createEmployeeTest() { EmployeeVO emp = new
	 * EmployeeVO(1,"Lokesh","Gupta","user@email.com");
	 * 
	 * manager.addEmployee(emp);
	 * 
	 * verify(dao, times(1)).addEmployee(emp); }
	 */
    
    public List<Accounts> getAccounts(){
    	 List<Accounts> list = new ArrayList<>();
         Accounts acntOne = new Accounts(10002L, "", "", 0.0);
         Accounts acntTwo = new Accounts(10003L, "", "", 0.0);
          
         list.add(acntOne);
         list.add(acntTwo);
         
         
         return list;
          
    	}
    }

