package com.cbh.cryptoservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@ComponentScan("/com.cbh.*")
public class CryptoserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoserviceApplication.class, args);
	}

}
