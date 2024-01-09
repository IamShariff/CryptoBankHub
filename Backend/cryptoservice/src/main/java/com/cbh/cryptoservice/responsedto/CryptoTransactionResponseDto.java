package com.cbh.cryptoservice.responsedto;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.cbh.cryptoservice.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoTransactionResponseDto {

	private Integer transactionId;
	private TransactionType type;
	private String cryptoId;
	private Integer quantity;
	private BigDecimal price;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createdAt;

}
