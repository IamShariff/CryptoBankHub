package com.cbh.cryptoservice.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer transactionId;
	@Enumerated(EnumType.STRING)
	private TransactionType type;
	private String cryptoId;
	private Integer quantity;
	private BigDecimal price;
	private Timestamp createdAt;

	@ManyToOne
	@JoinColumn(name = "portfolio_id")
	private CryptoPortfolio portfolio;

}
