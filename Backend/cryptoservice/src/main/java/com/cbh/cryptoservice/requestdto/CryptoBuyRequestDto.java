package com.cbh.cryptoservice.requestdto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoBuyRequestDto {
	
	private String cryptoId;
	private BigDecimal price;
	private Integer amount;
	//taken in 2d step in frontend
	private String bankName;
	private Long accountNumber;

}
