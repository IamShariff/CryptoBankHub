package com.cbh.bankservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bankAccountNumber;

	private String userId;
	
	private double balance;
	
	@Enumerated(EnumType.STRING)
	private BankName bankName;
	
	@Enumerated(EnumType.STRING)
	private AccountType bankAccountType;
	
	private String bankAccountStatus;

}
