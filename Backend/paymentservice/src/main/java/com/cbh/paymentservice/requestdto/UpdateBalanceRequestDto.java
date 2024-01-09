package com.cbh.paymentservice.requestdto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBalanceRequestDto {

	private Long bankAccountNumber;
	private String userId;
	private double amount;
	private String transactionType;

}