package com.example.transaction_service.repository;

import com.example.transaction_service.entity.TransactionEntity;
import com.example.transaction_service.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    // Trouver les transactions par compte (source ou destination)
    @Query("SELECT t FROM TransactionEntity t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId")
    Page<TransactionEntity> findByAccountId(@Param("accountId") Long accountId, Pageable pageable);

    // Trouver les transactions par utilisateur
    Page<TransactionEntity> findByInitiatedBy(int createdBy, Pageable pageable);

    // Trouver les transactions par statut
    List<TransactionEntity> findByStatus(TransactionStatus status);

    // Trouver les transactions par période
    @Query("SELECT t FROM TransactionEntity t WHERE t.createdAt BETWEEN :startDate AND :endDate")
    List<TransactionEntity> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);

    // Statistiques par utilisateur
    @Query("SELECT COUNT(t) FROM TransactionEntity t WHERE t.initiatedBy = :username AND t.status = :status")
    Long countByCreatedByAndStatus(@Param("username") String username, @Param("status") TransactionStatus status);
}
