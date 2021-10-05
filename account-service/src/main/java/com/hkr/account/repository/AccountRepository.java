package com.hkr.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hkr.account.model.Accounts;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Long> {

}
