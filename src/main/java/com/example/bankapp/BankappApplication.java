package com.example.bankapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankappApplication {

	private static final Logger logger = LoggerFactory.getLogger(BankappApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BankappApplication.class, args);
		logger.info("SKBBank Application started successfully");
	}
}
