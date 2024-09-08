package com.crypto.transaction.repository;

import com.crypto.transaction.entity.Transaction;
import com.crypto.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    Optional<Transaction> findByTransactionId(String transactionId);
    List<Transaction> findByWallet(Wallet wallet);
}