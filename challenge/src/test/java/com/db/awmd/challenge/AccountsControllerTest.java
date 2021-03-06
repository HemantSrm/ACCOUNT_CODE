package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.math.BigDecimal;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AccountsControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private AccountsService accountsService;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Autowired
	private AccountsRepository repository;

	@Mock
	private AccountsRepositoryInMemory accountsRepositoryInMemoryObj;

	/*
	 * @Before public void prepareMockMvc() { this.mockMvc =
	 * webAppContextSetup(this.webApplicationContext).build();
	 * 
	 * // Reset the existing accounts before each test.
	 * accountsService.getAccountsRepository().clearAccounts(); }
	 */
	
	@Before
	  public void prepareMockMvc() {
	    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

	    // Reset the existing accounts before each test.
	    accountsService.getAccountsRepository().clearAccounts();
	    String sourceAccountId = "1";
		String destAccountId = "2";
		
		Account srcAccount = repository.getAccount(sourceAccountId);
	    Account destinationAccount = repository.getAccount(destAccountId);
	    
	    if(Objects.isNull(srcAccount)) {
	    	repository.createAccount(new Account(sourceAccountId, new BigDecimal("1000.00")));
	    }
	    if(Objects.isNull(destinationAccount)) {	    	
	    	repository.createAccount(new Account(destAccountId, new BigDecimal("1001.00")));
	    }
	    
	  }

	@Test
	public void createAccount() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());

		Account account = accountsService.getAccount("Id-123");
		assertThat(account.getAccountId()).isEqualTo("Id-123");
		assertThat(account.getBalance()).isEqualByComparingTo("1000");
	}

	@Test
	public void createDuplicateAccount() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());

		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isBadRequest());
	}

	@Test
	public void createAccountNoAccountId() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"balance\":1000}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void createAccountNoBalance() throws Exception {
		this.mockMvc.perform(
				post("/v1/accounts").contentType(MediaType.APPLICATION_JSON).content("{\"accountId\":\"Id-123\"}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void createAccountNoBody() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void createAccountNegativeBalance() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"Id-123\",\"balance\":-1000}")).andExpect(status().isBadRequest());
	}

	@Test
	public void createAccountEmptyAccountId() throws Exception {
		this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
				.content("{\"accountId\":\"\",\"balance\":1000}")).andExpect(status().isBadRequest());
	}

	@Test
	public void getAccount() throws Exception {
		String uniqueAccountId = "Id-" + System.currentTimeMillis();
		Account account = new Account(uniqueAccountId, new BigDecimal("123.45"));
		this.accountsService.createAccount(account);
		this.mockMvc.perform(get("/v1/accounts/" + uniqueAccountId)).andExpect(status().isOk())
				.andExpect(content().string("{\"accountId\":\"" + uniqueAccountId + "\",\"balance\":123.45}"));
	}

	@Test
	public void testTransferAmountBetweenAccounts() throws Exception {
		 
			
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post("/v1/accounts/transfer-fund").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"srcAcctId\":\"1\",\"transferfund\":100.0000,\"destAcctId\":\"2\"}")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");
		

		this.mockMvc.perform(builder).andExpect(status().isOk());

	}
	
	  
	  @Test
	  public void testTransferAccountNotFound() throws Exception {
		  MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
					.post("/v1/accounts/transfer-fund").contentType(MediaType.APPLICATION_JSON_VALUE)
	                .content("{\"srcAcctId\":\"111\",\"transferfund\"100.0000,\"destAcctId\":\"2\"}")
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8");

	    
	    this.mockMvc.perform(builder)
	      .andExpect(status().isBadRequest());  
	  } 

	  @Test
	  public void testInvalidTransferAmount() throws Exception {
		
		
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
				.post("/v1/accounts/transfer-fund").contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"srcAcctId\":\"1\",\"transferfund\"-100.0000,\"destAcctId\":\"2\"}")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8");

		this.mockMvc.perform(builder).andExpect(status().isBadRequest());
	  }  
	  
	  @Test
	  public void testInsufficientTransferAmount() throws Exception {

		  //setAccount();
			
			MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
					.post("/v1/accounts/transfer-fund").contentType(MediaType.APPLICATION_JSON_VALUE)
	                .content("{\"srcAcctId\":\"1\",\"transferfund\":10000.0000,\"destAcctId\":\"2\"}")
	                .accept(MediaType.APPLICATION_JSON)
	                .characterEncoding("UTF-8");

			this.mockMvc.perform(builder).andExpect(status().isBadRequest());
	  }  
	  
	

}
