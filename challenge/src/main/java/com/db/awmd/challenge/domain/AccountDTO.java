package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class AccountDTO {

	@NotNull
	@NotEmpty(message = "Source account id cannot be empty")
	private String srcAccId;

	@NotNull
	@NotEmpty(message = "Destination account id cannot be empty")
	private String destAcctId;

	private BigDecimal transferfund;

	@JsonCreator
	public AccountDTO(@JsonProperty("srcAcctId") String accountId, @JsonProperty("destAcctId") String destAccId,
			@JsonProperty("transferfund") BigDecimal transferfund) {
		this.srcAccId = accountId;
		this.destAcctId = destAccId;
		this.transferfund = transferfund;
	}

}
