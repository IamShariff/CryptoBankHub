package com.cbh.bankservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.cbh.*")
@EnableDiscoveryClient
public class BankserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankserviceApplication.class, args);
	}

}
