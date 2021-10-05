package com.hkr.account.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction  implements Serializable {

  
	private static final long serialVersionUID = 1L;


    private List<Accounts> accountList;

    private Double amount;

    public List<Accounts> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Accounts> accountList) {
        this.accountList = accountList;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}