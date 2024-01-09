package com.cbh.cryptoservice.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.cbh.cryptoservice.config.CryptoTransactionSpecifications;
import com.cbh.cryptoservice.config.WebclientConfig;
import com.cbh.cryptoservice.model.Crypto;
import com.cbh.cryptoservice.model.CryptoTransaction;
import com.cbh.cryptoservice.model.CryptoPortfolio;
import com.cbh.cryptoservice.model.TransactionType;
import com.cbh.cryptoservice.repository.CryptoRepository;
import com.cbh.cryptoservice.repository.CryptoTransactionRepository;
import com.cbh.cryptoservice.repository.CryptoPortfolioRepository;
import com.cbh.cryptoservice.responsedto.CryptoHistoryResponseDto;
import com.cbh.cryptoservice.responsedto.CryptoInfoResponseDto;
import com.cbh.cryptoservice.responsedto.CryptoTransactionResponseDto;
import com.cbh.cryptoservice.responsedto.PortfolioResponseDto;
import com.cbh.cryptoservice.responsedto.TransactionFromPaymentResponseDto;
import com.cbh.cryptoservice.service.CryptoService;
import com.cbh.exceptionservice.exception.NotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoServiceImpl implements CryptoService {

	private final CryptoRepository cryptoRepository;
	private final WebclientConfig webclientConfig;
	private final CryptoPortfolioRepository portfolioRepository;
	private final CryptoTransactionRepository cryptoTransactionRepository;
	private final ModelMapper mapper;

	@Override
	@Scheduled(cron = "0 0/5 * * * *")
	public void fetchAndSaveInDb() {

		log.info("Updating crypto with schedular");
		List<Crypto> cryptoList = webclientConfig.getList("/v2/assets", new TypeReference<List<Crypto>>() {
		});
		cryptoList.forEach(crypto -> {
			crypto.convertPriceUsdToInr();
			crypto.convertMarketCapToInr();
			crypto.convertVolumeToInr();
			crypto.convertVwapToInr();
		});
		cryptoRepository.saveAll(cryptoList);
	}

	@Override
	public CryptoInfoResponseDto cryptoInfo(String id) {
		Optional<Crypto> findById = cryptoRepository.findById(id);
		if (findById.isEmpty()) {
			throw new NotFoundException("id", "No cryptocurrency exist with given id");
		}
		return mapper.map(findById.get(), CryptoInfoResponseDto.class);
	}

	@Override
	public void updatePortfolio(TransactionFromPaymentResponseDto transaction) {
		portfolioRepository.findByUserIdAndCryptoId(transaction.getUserId(), transaction.getCryptoId())
				.ifPresentOrElse(existingPortfolio -> {
					BigDecimal cryptoCurrentPrice = getCryptoCurrentPrice(transaction.getCryptoId());

					if (TransactionType.BUY.equals(transaction.getType())) {
						log.info("In Buy logic");
						BigDecimal buyAtAvgPrice = existingPortfolio.getBuyAtAvgPrice();
						BigDecimal newAvgPrice = transaction.getPrice().add(buyAtAvgPrice)
								.divide(BigDecimal.valueOf(2));
						int newHoldings = existingPortfolio.getHoldings() + transaction.getTotalCrypto();
						BigDecimal pnl = (cryptoCurrentPrice.subtract(newAvgPrice))
								.multiply(BigDecimal.valueOf(transaction.getTotalCrypto()));
						log.info("Total pnl: {}", pnl);
						existingPortfolio.setBuyAtAvgPrice(newAvgPrice);
						existingPortfolio.setHoldings(newHoldings);
						existingPortfolio.setPnl(existingPortfolio.getPnl().add(pnl));

					} else if (TransactionType.SELL.equals(transaction.getType())) {
						log.info("In Sell logic");
						BigDecimal newAvgPrice = existingPortfolio.getBuyAtAvgPrice();
						int newHoldings = existingPortfolio.getHoldings() - transaction.getTotalCrypto();

						BigDecimal pnl = (cryptoCurrentPrice.subtract(newAvgPrice))
								.multiply(BigDecimal.valueOf(transaction.getTotalCrypto()));
						log.info("Total pnl: {}", pnl);

						existingPortfolio.setHoldings(newHoldings);
						existingPortfolio.setPnl(existingPortfolio.getPnl().add(pnl));
					}

					existingPortfolio.setCurrentPrice(cryptoCurrentPrice);

					CryptoPortfolio updatedPortfolio = portfolioRepository.save(existingPortfolio);
					addCryptoTransaction(transaction, updatedPortfolio);

				}, () -> {
					CryptoPortfolio portfolio = new CryptoPortfolio();
					portfolio.setBuyAtAvgPrice(transaction.getPrice());
					portfolio.setCryptoId(transaction.getCryptoId());
					portfolio.setUserId(transaction.getUserId());
					portfolio.setHoldings(transaction.getTotalCrypto());
					BigDecimal cryptoCurrentPrice = getCryptoCurrentPrice(transaction.getCryptoId());
					portfolio.setCurrentPrice(cryptoCurrentPrice);

					BigDecimal pnl = (cryptoCurrentPrice.subtract(transaction.getPrice()))
							.multiply(BigDecimal.valueOf(transaction.getTotalCrypto()));
					log.info("Total pnl {}", pnl);

					portfolio.setPnl(pnl);
					CryptoPortfolio newPortfolio = portfolioRepository.save(portfolio);
					addCryptoTransaction(transaction, newPortfolio);
				});
	}

	private BigDecimal getCryptoCurrentPrice(String cryptoId) {
		return cryptoRepository.findById(cryptoId).map(Crypto::getPriceInr)
				.orElseThrow(() -> new NotFoundException("cryptoId", "Crypto not found"));
	}

	private void addCryptoTransaction(TransactionFromPaymentResponseDto transaction, CryptoPortfolio portfolio) {
		CryptoTransaction newTransaction = new CryptoTransaction();
		newTransaction.setCreatedAt(Timestamp.from(Instant.now()));
		newTransaction.setCryptoId(transaction.getCryptoId());
		newTransaction.setPrice(transaction.getPrice());
		newTransaction.setQuantity(transaction.getTotalCrypto());
		newTransaction.setType(transaction.getType());
		newTransaction.setPortfolio(portfolio);
		cryptoTransactionRepository.save(newTransaction);

	}

	@Override
	public List<PortfolioResponseDto> fetchUserPortfolio(String userId) {
		Optional<List<CryptoPortfolio>> optionalPortfolioList = portfolioRepository.findByUserId(userId);
		if (optionalPortfolioList.isPresent()) {
			List<CryptoPortfolio> portfolioList = optionalPortfolioList.get();
			List<PortfolioResponseDto> responseDtoList = new ArrayList<>();

			for (CryptoPortfolio portfolio : portfolioList) {
				PortfolioResponseDto responseDto = mapper.map(portfolio, PortfolioResponseDto.class);
				responseDtoList.add(responseDto);
			}
			return responseDtoList;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public List<CryptoTransactionResponseDto> fetchTransactions(String portfolioId) {

		Specification<CryptoTransaction> spec = CryptoTransactionSpecifications.byPortfolioId(portfolioId);
		Optional<List<CryptoTransaction>> findByPortfolioId = cryptoTransactionRepository.findAll(spec);

		if (findByPortfolioId.isPresent()) {
			List<CryptoTransaction> transactionList = findByPortfolioId.get();
			List<CryptoTransactionResponseDto> responseDtoList = new ArrayList<>();

			for (CryptoTransaction transaction : transactionList) {
				CryptoTransactionResponseDto responseDto = mapper.map(transaction, CryptoTransactionResponseDto.class);
				responseDtoList.add(responseDto);
			}
			return responseDtoList;
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Boolean checkCryptoQuantity(String userId, String cryptoId, Integer quantity) {
		return portfolioRepository.findByUserIdAndCryptoId(userId, cryptoId)
				.map(portfolio -> portfolio.getHoldings() >= quantity)
				.orElseThrow(() -> new NotFoundException("User or CryptoId", "Not found"));
	}

	@Override
	public List<CryptoHistoryResponseDto> cryptoHistory(String cryptoId, String interval) {

		List<String> allowedIntervals = Arrays.asList("m1", "m5", "m15", "m30", "h1", "h2", "h6", "h12", "d1");
		if (!allowedIntervals.contains(interval)) {
			throw new NotFoundException("interval",
					"Invalid interval. Allowed values are: m1, m5, m15, m30, h1, h2, h6, h12, d1");
		}

		List<CryptoHistoryResponseDto> cryptoHistoryList = webclientConfig.getList(
				String.format("/v2/assets/%s/history?interval=%s", cryptoId, interval),
				new TypeReference<List<CryptoHistoryResponseDto>>() {
				});
		cryptoHistoryList.forEach(cryptoHistory -> {
			cryptoHistory.formatPriceUsd();
			cryptoHistory.convertPriceUsdToInr();
		});

		return cryptoHistoryList;
	}
}
