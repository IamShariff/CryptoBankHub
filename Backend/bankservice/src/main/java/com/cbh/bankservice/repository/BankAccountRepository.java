package com.cbh.bankservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cbh.bankservice.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount,Long>{

	List<BankAccount> findByUserId(String userId);
	
}
