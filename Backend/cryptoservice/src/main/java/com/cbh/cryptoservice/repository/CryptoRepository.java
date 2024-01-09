package com.cbh.cryptoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cbh.cryptoservice.model.Crypto;

@Repository
public interface CryptoRepository extends JpaRepository<Crypto, String> {

}
