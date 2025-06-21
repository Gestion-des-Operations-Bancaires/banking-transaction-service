package com.example.transaction_service.service;

import com.example.transaction_service.dto.AccountDTO;
import com.example.transaction_service.dto.TransactionDTO;
import com.example.transaction_service.dto.TransactionResponsDTO;
import com.example.transaction_service.entity.TransactionEntity;
import com.example.transaction_service.entity.TransactionStatus;
import com.example.transaction_service.entity.TransactionType;
import com.example.transaction_service.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class TransactionService {

    private TransactionRepository transactionRepository;

    private AccountServiceClient accountServiceClient;

    public TransactionResponsDTO createTransaction(TransactionDTO request, int userId, String authToken) {

        // Fetch only the 'from' account if needed (e.g., to check balance)
        if (request.getType() == TransactionType.TRANSFER || request.getType() == TransactionType.WITHDRAWAL || request.getType() == TransactionType.PAYMENT) {
            AccountDTO fromAccount = accountServiceClient.getAccountById(request.getFromAccountId(), authToken);

            if (fromAccount == null) {
                throw new RuntimeException("Compte source introuvable");
            }

            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                throw new RuntimeException("Solde insuffisant");
            }
        }

        // Create transaction
        TransactionEntity transaction = new TransactionEntity(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount(),
                request.getType(),
                request.getDescription(),
                userId
        );

        transaction = transactionRepository.save(transaction);

//        try {
//            switch (request.getType()) {
//                case TRANSFER:
//                    accountServiceClient.debitAccount(request.getFromAccountId(), request.getAmount(), authToken);
//                    accountServiceClient.creditAccount(request.getToAccountId(), request.getAmount(), authToken);
//                    break;
//                case DEPOSIT:
//                    accountServiceClient.creditAccount(request.getToAccountId(), request.getAmount(), authToken);
//                    break;
//                case WITHDRAWAL:
//                case PAYMENT:
//                    accountServiceClient.debitAccount(request.getFromAccountId(), request.getAmount(), authToken);
//                    break;
//            }
//
//            transaction.setStatus(TransactionStatus.COMPLETED);
//
//        } catch (Exception e) {
//            transaction.setStatus(TransactionStatus.FAILED);
//            throw new RuntimeException("Erreur lors de la transaction : " + e.getMessage());
//        }

        transaction = transactionRepository.save(transaction);
        return mapToResponse(transaction);
    }


    public Page<TransactionResponsDTO> getUserTransactions(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TransactionEntity> transactions = transactionRepository.findByInitiatedBy(userId, pageable);
        return transactions.map(this::mapToResponse);
    }

    public TransactionResponsDTO getTransactionById(Long id) {
        TransactionEntity transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction introuvable"));
        return mapToResponse(transaction);
    }

    public Page<TransactionResponsDTO> getAccountTransactions(Long accountId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<TransactionEntity> transactions = transactionRepository.findByAccountId(accountId, pageable);
        return transactions.map(this::mapToResponse);
    }

    public List<TransactionResponsDTO> getPendingTransactions() {
        List<TransactionEntity> transactions = transactionRepository.findByStatus(TransactionStatus.PENDING);
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponsDTO mapToResponse(TransactionEntity transaction) {
        return new TransactionResponsDTO(
                transaction.getId(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getCreatedAt(),
                transaction.getInitiatedBy()
        );
    }
}
