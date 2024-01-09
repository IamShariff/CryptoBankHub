package com.cbh.bankservice.requestdto;

import com.cbh.bankservice.model.AccountType;
import com.cbh.bankservice.model.BankName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddBankAccountRequestDto {
	
	private String userId;
	private BankName bankName;
	private AccountType bankAccountType;
}
