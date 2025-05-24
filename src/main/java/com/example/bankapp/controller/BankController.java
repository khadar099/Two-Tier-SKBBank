package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankController {

    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountService.findAccountByUsername(username);
            logger.info("Accessed dashboard for user: {}", username);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            logger.error("Dashboard fetch failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@RequestParam String username, @RequestParam String password) {
        try {
            accountService.registerAccount(username, password);
            logger.info("User registered: {}", username);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (RuntimeException e) {
            logger.error("Registration failed for user {}: {}", username, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestParam BigDecimal amount) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountService.findAccountByUsername(username);
            accountService.deposit(account, amount);
            logger.info("User {} deposited amount {}", username, amount);
            return ResponseEntity.ok("Deposit successful");
        } catch (Exception e) {
            logger.error("Deposit failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestParam BigDecimal amount) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountService.findAccountByUsername(username);
            accountService.withdraw(account, amount);
            logger.info("User {} withdrew amount {}", username, amount);
            return ResponseEntity.ok("Withdrawal successful");
        } catch (RuntimeException e) {
            logger.warn("Withdrawal failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> transactionHistory() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account account = accountService.findAccountByUsername(username);
            logger.info("User {} viewed transaction history", username);
            return ResponseEntity.ok(accountService.getTransactionHistory(account));
        } catch (Exception e) {
            logger.error("Failed to fetch transactions: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferAmount(@RequestParam String toUsername, @RequestParam BigDecimal amount) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Account fromAccount = accountService.findAccountByUsername(username);
            accountService.transferAmount(fromAccount, toUsername, amount);
            logger.info("User {} transferred amount {} to {}", username, amount, toUsername);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Transfer successful");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Transfer failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
