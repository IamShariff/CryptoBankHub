package com.cbh.bankservice.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBalanceRequestDto {
	
	private Long bankAccountNumber;
	private String userId;
	private double amount;
	private String transactionType;

}
