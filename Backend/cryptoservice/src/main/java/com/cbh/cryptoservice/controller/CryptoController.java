package com.cbh.cryptoservice.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbh.cryptoservice.responsedto.CryptoHistoryResponseDto;
import com.cbh.cryptoservice.responsedto.CryptoInfoResponseDto;
import com.cbh.cryptoservice.responsedto.CryptoTransactionResponseDto;
import com.cbh.cryptoservice.responsedto.PortfolioResponseDto;
import com.cbh.cryptoservice.service.CryptoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Tag(name = "CyptoController", description = "Contains all the end point related to crypto operations")
@RequestMapping("/crypto")
public class CryptoController {

	private final CryptoService cryptoService;

	@GetMapping("/fetch-coincap")
	@Operation(description = "Fetch crypto from Coincap and save in the database")
	ResponseEntity<Void> fetchAndSaveCryptoFromCoinGecko() {
		cryptoService.fetchAndSaveInDb();
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/info/{id}")
	@Operation(description = "Fetch crypto info by its Id from the database")
	ResponseEntity<CryptoInfoResponseDto> cryptoInfoById(@PathVariable String id) {
		return ResponseEntity.ok(cryptoService.cryptoInfo(id));
	}

	@GetMapping("/portfolio/{userId}")
	@Operation(description = "Fetch user portfolio")
	ResponseEntity<List<PortfolioResponseDto>> userCryptoPortfolio(@PathVariable String userId) {
		return ResponseEntity.ok(cryptoService.fetchUserPortfolio(userId));
	}

	@GetMapping("/transactions/{portfolioId}")
	@Operation(description = "Get all transactions of a crypto")
	ResponseEntity<List<CryptoTransactionResponseDto>> cryptoTransactions(@PathVariable String portfolioId) {
		return ResponseEntity.ok(cryptoService.fetchTransactions(portfolioId));
	}

	@GetMapping("/check-quantity/{userId}/{cryptoId}/{quantity}")
	@Operation(description = "Get wheather user have enough crypto quantity in their portfolio")
	ResponseEntity<Boolean> cryptoQunantity(@PathVariable String userId, @PathVariable String cryptoId,
			@PathVariable Integer quantity) {
		return ResponseEntity.ok(cryptoService.checkCryptoQuantity(userId, cryptoId, quantity));
	}

	@GetMapping("/history/{cryptoId}/{interval}")
	@Operation(description = "Get history of crypto for given interval of time")
	ResponseEntity<List<CryptoHistoryResponseDto>> getCryptoHistory(@PathVariable String cryptoId,
			@PathVariable String interval) {
		return ResponseEntity.ok(cryptoService.cryptoHistory(cryptoId, interval));
	}

}
