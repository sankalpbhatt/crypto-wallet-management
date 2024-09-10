package com.crypto.wallet.repository;

import com.crypto.wallet.entity.WalletBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletBalanceRepository extends JpaRepository<WalletBalance, Long> {
}
