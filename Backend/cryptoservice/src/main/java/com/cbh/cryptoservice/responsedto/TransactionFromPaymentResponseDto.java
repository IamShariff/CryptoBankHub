package com.cbh.cryptoservice.responsedto;

import java.math.BigDecimal;

import com.cbh.cryptoservice.model.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionFromPaymentResponseDto {

	private String userId;
	private String cryptoId;
	private BigDecimal price;
	private Integer totalCrypto;
	private TransactionType type;
}
