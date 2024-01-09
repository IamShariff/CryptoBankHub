package com.cbh.cryptoservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cbh.cryptoservice.model.CryptoPortfolio;

public interface CryptoPortfolioRepository extends JpaRepository<CryptoPortfolio, Integer> {

	Optional<CryptoPortfolio> findByUserIdAndCryptoId(String userId, String cryptoId);

	Optional<List<CryptoPortfolio>> findByUserId(String userId);

	Optional<CryptoPortfolio> findByCryptoId(String cryptoId);

}
