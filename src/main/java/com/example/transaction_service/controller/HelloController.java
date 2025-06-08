package com.example.transaction_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class HelloController {

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/hello")
    public String sayHello() {
        Object userId = request.getAttribute("userId");
        if (userId instanceof Integer) {
            return "Hello, World! Your ID is: " + userId;
        }
        return "Hello, World! (No ID found)";
    }
}
