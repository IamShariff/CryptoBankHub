package com.cbh.bankservice.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckBalanceRequestDto {
	
	private Long bankAccountNumber;
	private String userId;
}
