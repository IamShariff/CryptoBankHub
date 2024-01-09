package com.cbh.paymentservice.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cbh.paymentservice.requestdto.CryptoTransactionRequestDto;
import com.cbh.paymentservice.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@Tag(name = "Payment Controller")
public class PaymentController {

	private final PaymentService paymentService;

	@PostMapping("/buy")
	@Operation(description = "Buy Crypto")
	ResponseEntity<String> buyCrypto(@RequestBody CryptoTransactionRequestDto cryptoTransactionRequestDto) {
		boolean success = paymentService.buy(cryptoTransactionRequestDto);
		return Optional.of(success).filter(Boolean::booleanValue)
				.map(result -> ResponseEntity.ok("Crypto bought successfully!"))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to buy crypto."));
	}

	@PostMapping("/sell")
	@Operation(description = "Sell Crypto")
	ResponseEntity<String> sellCrypto(@RequestBody CryptoTransactionRequestDto cryptoTransactionRequestDto) {
		boolean success = paymentService.sell(cryptoTransactionRequestDto);
		return Optional.of(success).filter(Boolean::booleanValue)
				.map(result -> ResponseEntity.ok("Crypto sell successfully!"))
				.orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to sell crypto."));
	}

}
