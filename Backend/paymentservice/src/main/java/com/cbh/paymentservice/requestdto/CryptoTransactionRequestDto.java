package com.cbh.paymentservice.requestdto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoTransactionRequestDto {

	@NotBlank
	private String userId;
	@NotBlank
	private String cryptoId;
	@NotNull
	private BigDecimal price;
	@NotNull
	private Integer quantity;
	@NotBlank
	private String bankName;
	@NotNull
	private Long accountNumber;

}
