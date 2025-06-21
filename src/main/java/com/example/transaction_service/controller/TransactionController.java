package com.example.transaction_service.controller;

import com.example.transaction_service.dto.TransactionDTO;
import com.example.transaction_service.dto.TransactionResponsDTO;
import com.example.transaction_service.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private HttpServletRequest request;

    @PostMapping
    public ResponseEntity<TransactionResponsDTO> createTransaction(
            @Valid @RequestBody TransactionDTO request,
            HttpServletRequest httpRequest) {

        Object userIdAttr = httpRequest.getAttribute("userId");
        String token = (String) httpRequest.getAttribute("token");

        if (userIdAttr == null) {
            return ResponseEntity.badRequest().body(null);
        }

        int userId = Integer.parseInt(userIdAttr.toString());
        TransactionResponsDTO response = transactionService.createTransaction(request, userId, token);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/my-transactions")
    public ResponseEntity<Page<TransactionResponsDTO>> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpRequest) {

        Object userIdAttr = httpRequest.getAttribute("userId");
        String token = (String) httpRequest.getAttribute("token");

        if (userIdAttr == null) {
            return ResponseEntity.badRequest().body(null);
        }

        int userId = Integer.parseInt(userIdAttr.toString());
        Page<TransactionResponsDTO> transactions = transactionService.getUserTransactions(userId, page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<Page<TransactionResponsDTO>> getAccountTransactions(
            @PathVariable Long accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<TransactionResponsDTO> transactions = transactionService.getAccountTransactions(accountId, page, size);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponsDTO> getTransaction(@PathVariable Long id) {
        TransactionResponsDTO transaction = transactionService.getTransactionById(id);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TransactionResponsDTO>> getPendingTransactions() {
        List<TransactionResponsDTO> transactions = transactionService.getPendingTransactions();
        return ResponseEntity.ok(transactions);
    }
}