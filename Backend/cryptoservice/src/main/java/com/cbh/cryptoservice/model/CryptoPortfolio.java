package com.cbh.cryptoservice.model;

import java.math.BigDecimal;
import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoPortfolio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer portfolioId;
	private String userId;
	private String cryptoId;
	private BigDecimal buyAtAvgPrice;
	private BigDecimal currentPrice;
	private Integer holdings;
	private BigDecimal pnl;

	@OneToMany(mappedBy = "portfolio")
	private List<CryptoTransaction> transactions;

}
