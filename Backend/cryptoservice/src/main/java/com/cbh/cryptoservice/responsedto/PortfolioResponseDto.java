package com.cbh.cryptoservice.responsedto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioResponseDto {
	
	private Integer portfolioId;
	private String cryptoId;
	private BigDecimal buyAtAvgPrice;
	private BigDecimal currentPrice;
	private Integer holdings;
	private BigDecimal pnl;

}
