package com.cbh.paymentservice.requestdto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CryptoTransactionEventDto {

	private String userId;
	private String cryptoId;
	private BigDecimal price;
	private Integer totalCrypto;
	private TransactionType type;
	
}
