package com.cbh.cryptoservice.service;

import java.util.List;

import com.cbh.cryptoservice.responsedto.CryptoHistoryResponseDto;
import com.cbh.cryptoservice.responsedto.CryptoInfoResponseDto;
import com.cbh.cryptoservice.responsedto.CryptoTransactionResponseDto;
import com.cbh.cryptoservice.responsedto.PortfolioResponseDto;
import com.cbh.cryptoservice.responsedto.TransactionFromPaymentResponseDto;

public interface CryptoService {

	void fetchAndSaveInDb();

	CryptoInfoResponseDto cryptoInfo(String id);

	void updatePortfolio(TransactionFromPaymentResponseDto transaction);

	List<PortfolioResponseDto> fetchUserPortfolio(String userId);

	List<CryptoTransactionResponseDto> fetchTransactions(String portfolioId);

	Boolean checkCryptoQuantity(String userId, String cryptoId, Integer quantity);

	List<CryptoHistoryResponseDto> cryptoHistory(String cryptoId, String interval);

}
