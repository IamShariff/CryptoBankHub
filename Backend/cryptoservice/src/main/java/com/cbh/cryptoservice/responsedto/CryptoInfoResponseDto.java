package com.cbh.cryptoservice.responsedto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoInfoResponseDto {

	/**
	 * Unique identifier for the asset.
	 */
	private String id;

	/**
	 * Rank in ascending order. The highest market cap receives rank 1.
	 */
	private String rank;

	/**
	 * Most common symbol used to identify this asset on an exchange.
	 */
	private String symbol;

	/**
	 * Proper name for the asset.
	 */
	private String name;

	/**
	 * Available supply for trading.
	 */
	private String supply;

	/**
	 * Total quantity of the asset issued.
	 */
	private String maxSupply;

	/**
	 * The direction and value change in the last 24 hours.
	 */
	private String changePercent24Hr;

	/**
	 * Explorer associated with the asset.
	 */
	private String explorer;

	/**
	 * Volume-weighted price based on real-time market data, translated to INR.
	 */
	private BigDecimal priceInr;

	/**
	 * Market capitalization in INR (supply x price).
	 */
	private BigDecimal marketCapInr;

	/**
	 * Quantity of trading volume represented in INR over the last 24 hours.
	 */
	private BigDecimal volumeInr;

	/**
	 * Volume Weighted Average Price in INR in the last 24 hours.
	 */
	private BigDecimal vwapInr;
}
