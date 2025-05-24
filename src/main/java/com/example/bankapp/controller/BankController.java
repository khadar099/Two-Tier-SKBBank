package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class BankController {

    private static final Logger logger = LoggerFactory.getLogger(BankController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        logger.info("Accessed dashboard for user: {}", username);
        model.addAttribute("account", account);
        return "dashboard";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerAccount(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            accountService.registerAccount(username, password);
            logger.info("User registered: {}", username);
            return "redirect:/login";
        } catch (RuntimeException e) {
            logger.error("Registration failed for user {}: {}", username, e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam BigDecimal amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        accountService.deposit(account, amount);
        logger.info("User {} deposited amount {}", username, amount);
        return "redirect:/dashboard";
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam BigDecimal amount, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);

        try {
            accountService.withdraw(account, amount);
            logger.info("User {} withdrew amount {}", username, amount);
        } catch (RuntimeException e) {
            logger.warn("Withdrawal failed for user {}: {}", username, e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", account);
            return "dashboard";
        }

        return "redirect:/dashboard";
    }

    @GetMapping("/transactions")
    public String transactionHistory(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("transactions", accountService.getTransactionHistory(account));
        logger.info("User {} viewed transaction history", username);
        return "transactions";
    }

    @PostMapping("/transfer")
    public String transferAmount(@RequestParam String toUsername, @RequestParam BigDecimal amount, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account fromAccount = accountService.findAccountByUsername(username);

        try {
            accountService.transferAmount(fromAccount, toUsername, amount);
            logger.info("User {} transferred amount {} to {}", username, amount, toUsername);
        } catch (RuntimeException e) {
            logger.warn("Transfer failed from {} to {}: {}", username, toUsername, e.getMessage());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("account", fromAccount);
            return "dashboard";
        }

        return "redirect:/dashboard";
    }
}
