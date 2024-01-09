package com.cbh.cryptoservice.config;

import org.springframework.data.jpa.domain.Specification;

import com.cbh.cryptoservice.model.CryptoTransaction;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CryptoTransactionSpecifications {

	public static Specification<CryptoTransaction> byPortfolioId(String portfolioId) {
		return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("portfolio").get("portfolioId"),
				portfolioId);
	}
}
