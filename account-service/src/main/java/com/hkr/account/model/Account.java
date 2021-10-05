package com.hkr.account.model;

import java.math.BigDecimal;

import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
public class Account {

 @SuppressWarnings("deprecation")
@NotNull
@NotEmpty
private final String accountId;

 @NotNull
@Min(value = 0, message = "Initial balance must be positive.")
private BigDecimal balance;

 public Account(String accountId) {
this.accountId = accountId;
this.balance = BigDecimal.ZERO;
}

 @JsonCreator
public Account(@JsonProperty("accountId") String accountId,
@JsonProperty("balance") BigDecimal balance) {
this.accountId = accountId;
this.balance = balance;
}

public BigDecimal getBalance() {
	return balance;
}

public void setBalance(BigDecimal balance) {
	this.balance = balance;
}

public String getAccountId() {
	return accountId;
}
 
 
}