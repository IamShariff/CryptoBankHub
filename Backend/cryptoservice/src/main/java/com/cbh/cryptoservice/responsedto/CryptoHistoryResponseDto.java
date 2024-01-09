package com.cbh.cryptoservice.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoHistoryResponseDto {

	@JsonProperty(access = Access.WRITE_ONLY)
	private BigDecimal priceUsd;
	private BigDecimal priceInr;
	private String time;
	private String circulatingSupply;
	private String date;

	// Custom method to format priceUsd to display only two decimal places
	public void formatPriceUsd() {
		if (priceUsd != null) {
			priceUsd = priceUsd.setScale(2, RoundingMode.HALF_UP);
		}
	}

	// Custom method to convert priceUsd to INR
	public void convertPriceUsdToInr() {
		if (priceUsd != null) {
			BigDecimal conversionRate = new BigDecimal("84");
			priceInr = priceUsd.multiply(conversionRate).setScale(2, RoundingMode.HALF_UP);
		}
	}
}
