package com.crypto.wallet.service.impl;

import com.crypto.common.entity.SequenceType;
import com.crypto.common.service.impl.SequenceGeneratorServiceImpl;
import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.request.UpdateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import com.crypto.wallet.mapper.WalletMapper;
import com.crypto.wallet.repository.WalletRepository;
import com.crypto.wallet.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
public class WalletServiceImpl extends SequenceGeneratorServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    @Transactional
    @Override
    public WalletResponse createWallet(CreateWalletRequest createWalletRequest) {
        Wallet wallet = walletMapper.mapRequestToEntity(createWalletRequest);
        String walletId = SequenceType.USER.getPrefix() + getNextSequenceValue(SequenceType.WALLET);
        wallet.setWalletId(walletId);
        return walletMapper.mapToResponseDto(walletRepository.save(wallet));
    }

    @Transactional(readOnly = true)
    @Override
    public WalletResponse getWalletById(String id) {
        return walletMapper.mapToResponseDto(walletRepository.findByWalletId(id)
                .orElseThrow(() -> new NoSuchElementException("Wallet not found")));
    }

    @Transactional
    @Override
    public WalletResponse updateWallet(String id, UpdateWalletRequest updateWalletRequest) {
        Wallet wallet = walletRepository.findByWalletId(id)
                .orElseThrow(() -> new NoSuchElementException("Wallet not found"));
        wallet.setBalance(updateWalletRequest.getBalance());
        wallet.setUpdatedDate(LocalDateTime.now());
        return walletMapper.mapToResponseDto(walletRepository.save(wallet));
    }

    @Transactional
    @Override
    public void deleteWallet(String id) {
        Wallet wallet = walletRepository.findByWalletId(id)
                .orElseThrow(() -> new NoSuchElementException("Wallet not found"));
        wallet.setDeletedDate(LocalDateTime.now());
        walletRepository.save(wallet);
    }
}
