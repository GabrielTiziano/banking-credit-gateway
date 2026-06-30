package com.gabrieltiziano.banking_credit_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankingCreditGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingCreditGatewayApplication.class, args);
	}

}
