package com.example.bankapp.controller;

import com.example.bankapp.model.Account;
import com.example.bankapp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String login() {
        // Return login view name (login.html in templates)
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Account account = accountService.findAccountByUsername(username);
        model.addAttribute("account", account);
        return "dashboard";  // dashboard.html template
    }
}
