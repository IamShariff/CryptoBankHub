package com.cbh.cryptoservice.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crypto {

	@Id
	private String id;
	private String rank;
	private String symbol;
	private String name;
	private String supply;
	private String maxSupply;
	
	@Transient
	private String marketCapUsd;
	
	@Transient
	private String volumeUsd24Hr;

	@Transient
	private String priceUsd;
	
	private String changePercent24Hr;
	@Transient
	
	private String vwap24Hr;
	private String explorer;
	private BigDecimal priceInr;
	private BigDecimal marketCapInr;
	private BigDecimal volumeInr;
	private BigDecimal vwapInr;

	// Custom method to convert priceUsd from String to BigDecimal
	public void convertPriceUsdToInr() {
		if (priceUsd != null) {
			priceInr = new BigDecimal(priceUsd).multiply(BigDecimal.valueOf(84));
		}
	}

	// Custom method to convert marketCapUsd to marketCapInr
	public void convertMarketCapToInr() {
		if (marketCapUsd != null) {
			marketCapInr = new BigDecimal(marketCapUsd).multiply(BigDecimal.valueOf(84));
		}
	}

	// Custom method to convert volumeUsd24Hr to volumeInr
	public void convertVolumeToInr() {
		if (volumeUsd24Hr != null) {
			volumeInr = new BigDecimal(volumeUsd24Hr).multiply(BigDecimal.valueOf(84));
		}
	}

	// Custom method to convert vwap24Hr to vwapInr
	public void convertVwapToInr() {
		if (vwap24Hr != null) {
			vwapInr = new BigDecimal(vwap24Hr).multiply(BigDecimal.valueOf(84));
		}
	}
}
