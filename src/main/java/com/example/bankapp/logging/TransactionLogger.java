package com.example.bankapp.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TransactionLogger {

    private static final Logger transactionLogger = LoggerFactory.getLogger("transaction-logger");

    public void logDeposit(String userId, double amount) {
        transactionLogger.info("Deposit | userId={} | amount={}", userId, amount);
    }

    public void logWithdraw(String userId, double amount) {
        transactionLogger.info("Withdraw | userId={} | amount={}", userId, amount);
    }

    public void logLogin(String userId) {
        transactionLogger.info("Login | userId={}", userId);
    }

    public void logRegistration(String userId) {
        transactionLogger.info("Registration | userId={}", userId);
    }
}
