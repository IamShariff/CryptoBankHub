package com.cbh.notificationservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cbh.notificationservice.model.Otp;

public interface OtpRepository extends MongoRepository<Otp, String> {

	Optional<Otp> findByEmail(String email);

}
