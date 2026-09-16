package com.example.bankapp.service;

import static net.logstash.logback.argument.StructuredArguments.kv;

import com.example.bankapp.model.Account;
import com.example.bankapp.model.Transaction;
import com.example.bankapp.repository.AccountRepository;
import com.example.bankapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class AccountService implements UserDetailsService {

    private static final Logger transactionLogger = LoggerFactory.getLogger("transaction-logger");

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Account findAccountByUsername(String username) {
        return accountRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public Account registerAccount(String username, String password) {
        if (accountRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password)); // Encrypt password
        account.setBalance(BigDecimal.ZERO); // Initial balance set to 0
        Account savedAccount = accountRepository.save(account);

        transactionLogger.info("Registered new account",
                kv("user", username),
                kv("status", "success"));

        return savedAccount;
    }

    public void deposit(Account account, BigDecimal amount) {
        try {
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);

            Transaction transaction = new Transaction(
                    amount,
                    "Deposit",
                    LocalDateTime.now(),
                    account
            );
            transactionRepository.save(transaction);

            transactionLogger.info("Deposit successful",
                    kv("user", account.getUsername()),
                    kv("amount", amount),
                    kv("transaction", "deposit"),
                    kv("status", "success"));

        } catch (Exception e) {
            transactionLogger.error("Deposit failed",
                    kv("user", account.getUsername()),
                    kv("amount", amount),
                    kv("transaction", "deposit"),
                    kv("status", "failed"),
                    kv("error", e.getMessage()));
            throw e;
        }
    }

    public void withdraw(Account account, BigDecimal amount) {
        try {
            if (account.getBalance().compareTo(amount) < 0) {
                transactionLogger.warn("Withdrawal failed due to insufficient funds",
                        kv("user", account.getUsername()),
                        kv("amount", amount),
                        kv("transaction", "withdrawal"),
                        kv("status", "failed"));
                throw new RuntimeException("Insufficient funds");
            }

            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);

            Transaction transaction = new Transaction(
                    amount,
                    "Withdrawal",
                    LocalDateTime.now(),
                    account
            );
            transactionRepository.save(transaction);

            transactionLogger.info("Withdrawal successful",
                    kv("user", account.getUsername()),
                    kv("amount", amount),
                    kv("transaction", "withdrawal"),
                    kv("status", "success"));

        } catch (Exception e) {
            if (!e.getMessage().equals("Insufficient funds")) {
                transactionLogger.error("Withdrawal failed",
                        kv("user", account.getUsername()),
                        kv("amount", amount),
                        kv("transaction", "withdrawal"),
                        kv("status", "failed"),
                        kv("error", e.getMessage()));
            }
            throw e;
        }
    }

    public List<Transaction> getTransactionHistory(Account account) {
        return transactionRepository.findByAccountId(account.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = findAccountByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("Username or Password not found");
        }
        return new Account(
                account.getUsername(),
                account.getPassword(),
                account.getBalance(),
                account.getTransactions(),
                authorities());
    }

    public Collection<? extends GrantedAuthority> authorities() {
        return Arrays.asList(new SimpleGrantedAuthority("USER"));
    }

    public void transferAmount(Account fromAccount, String toUsername, BigDecimal amount) {
        try {
            if (fromAccount.getBalance().compareTo(amount) < 0) {
                transactionLogger.warn("Transfer failed due to insufficient funds",
                        kv("fromUser", fromAccount.getUsername()),
                        kv("toUser", toUsername),
                        kv("amount", amount),
                        kv("transaction", "transfer"),
                        kv("status", "failed"));
                throw new RuntimeException("Insufficient funds");
            }

            Account toAccount = accountRepository.findByUsername(toUsername)
                    .orElseThrow(() -> new RuntimeException("Recipient account not found"));

            // Deduct from sender's account
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
            accountRepository.save(fromAccount);

            // Add to recipient's account
            toAccount.setBalance(toAccount.getBalance().add(amount));
            accountRepository.save(toAccount);

            // Create transaction records for both accounts
            Transaction debitTransaction = new Transaction(
                    amount,
                    "Transfer Out to " + toAccount.getUsername(),
                    LocalDateTime.now(),
                    fromAccount
            );
            transactionRepository.save(debitTransaction);

            Transaction creditTransaction = new Transaction(
                    amount,
                    "Transfer In from " + fromAccount.getUsername(),
                    LocalDateTime.now(),
                    toAccount
            );
            transactionRepository.save(creditTransaction);

            transactionLogger.info("Transfer successful",
                    kv("fromUser", fromAccount.getUsername()),
                    kv("toUser", toUsername),
                    kv("amount", amount),
                    kv("transaction", "transfer"),
                    kv("status", "success"));

        } catch (Exception e) {
            transactionLogger.error("Transfer failed",
                    kv("fromUser", fromAccount.getUsername()),
                    kv("toUser", toUsername),
                    kv("amount", amount),
                    kv("transaction", "transfer"),
                    kv("status", "failed"),
                    kv("error", e.getMessage()));
            throw e;
        }
    }
}
