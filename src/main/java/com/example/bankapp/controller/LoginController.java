package com.example.bankapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Return the name of the login view (e.g., login.html or login.jsp)
        return "login";
    }
}
