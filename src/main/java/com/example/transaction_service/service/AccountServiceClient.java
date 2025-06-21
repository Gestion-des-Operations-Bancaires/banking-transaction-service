package com.example.transaction_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import com.example.transaction_service.dto.AccountDTO;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@FeignClient(name = "account-service", url = "${microservices.account-service.url}")
public interface AccountServiceClient {

    @GetMapping("/account/{id}")
    AccountDTO getAccountById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);

    @PutMapping("/api/accounts/{id}/debit")
    void debitAccount(@PathVariable("id") Long accountId,
                      @RequestParam BigDecimal amount,
                      @RequestHeader("Authorization") String token);

    @PutMapping("/api/accounts/{id}/credit")
    void creditAccount(@PathVariable("id") Long accountId,
                       @RequestParam BigDecimal amount,
                       @RequestHeader("Authorization") String token);

    @GetMapping("/account/customer/{userId}")
    List<AccountDTO> getAccountsByUserId(@PathVariable("userId") String userId,
                                         @RequestHeader("Authorization") String token);
}
