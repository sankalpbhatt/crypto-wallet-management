package com.crypto.wallet.service.impl;

import com.crypto.common.entity.SequenceType;
import com.crypto.common.service.impl.SequenceGeneratorServiceImpl;
import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.service.UserService;
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

@Service
public class WalletServiceImpl extends SequenceGeneratorServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final UserService userService;

    public WalletServiceImpl(WalletRepository walletRepository,
                             WalletMapper walletMapper,
                             UserService userService) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.userService = userService;
    }

    @Transactional
    @Override
    public WalletResponse createWallet(CreateWalletRequest createWalletRequest) {
        UserResponse userResponse = userService.getUser(createWalletRequest.getUserId());
        Wallet wallet = walletMapper.mapRequestToEntity(createWalletRequest, userResponse.internalId());
        String walletId = SequenceType.WALLET.getPrefix() + getNextSequenceValue(SequenceType.WALLET);
        wallet.setWalletId(walletId);
        return walletMapper
                .mapToResponseDto(walletRepository.save(wallet), createWalletRequest.getUserId());
    }

    @Transactional(readOnly = true)
    @Override
    public WalletResponse getWalletById(String id) {
        Wallet wallet = walletRepository.findByWalletId(id)
                .orElseThrow(() -> new MyServiceException("Wallet not found", ErrorCode.BUSINESS_ERROR));
        UserResponse userResponse = userService.getUserByInternalId(wallet.getUserId());
        return walletMapper.mapToResponseDto(wallet, userResponse.id());
    }

    @Transactional
    @Override
    public WalletResponse updateWallet(String id, UpdateWalletRequest updateWalletRequest) {
        Wallet wallet = walletRepository.findByWalletId(id)
                .orElseThrow(() -> new MyServiceException("Wallet not found", ErrorCode.BUSINESS_ERROR));
        switch (updateWalletRequest.getOperation()){
            case ADD -> wallet.setBalance(wallet.getBalance().add(updateWalletRequest.getBalance()));
            case SUBTRACT -> wallet.setBalance(wallet.getBalance().subtract(updateWalletRequest.getBalance()));
        }
        wallet.setUpdatedDate(LocalDateTime.now());
        UserResponse userResponse = userService.getUserByInternalId(wallet.getUserId());
        return walletMapper.mapToResponseDto(walletRepository.save(wallet), userResponse.id());
    }

    @Transactional
    @Override
    public void deleteWallet(String id) {
        Wallet wallet = walletRepository.findByWalletId(id)
                .orElseThrow(() -> new MyServiceException("Wallet not found", ErrorCode.BUSINESS_ERROR));
        wallet.setDeletedAt(LocalDateTime.now());
        walletRepository.save(wallet);
    }
}
