package com.cbh.cryptoservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cbh.cryptoservice.model.CryptoTransaction;

public interface CryptoTransactionRepository extends JpaRepository<CryptoTransaction, Integer> {

	//Optional<List<CryptoTransaction>> findByPortfolioPortfolioId(String portfolioId);

	Optional<List<CryptoTransaction>> findAll(Specification<CryptoTransaction> spec);

}
