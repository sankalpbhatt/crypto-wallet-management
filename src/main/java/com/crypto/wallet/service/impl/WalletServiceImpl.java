package com.crypto.wallet.service.impl;

import com.crypto.common.entity.SequenceType;
import com.crypto.common.service.impl.SequenceGeneratorServiceImpl;
import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import com.crypto.user.dto.UserResponse;
import com.crypto.user.service.UserService;
import com.crypto.util.CoinGekoClient;
import com.crypto.wallet.dto.Currency;
import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.request.UpdateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import com.crypto.wallet.entity.WalletBalance;
import com.crypto.wallet.mapper.WalletMapper;
import com.crypto.wallet.repository.WalletBalanceRepository;
import com.crypto.wallet.repository.WalletRepository;
import com.crypto.wallet.service.WalletService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class WalletServiceImpl extends SequenceGeneratorServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final UserService userService;
    private final CoinGekoClient coinGekoClient;
    private final WalletBalanceRepository walletBalanceRepository;

    public WalletServiceImpl(WalletRepository walletRepository,
                             WalletMapper walletMapper,
                             UserService userService,
                             CoinGekoClient coinGekoClient,
                             WalletBalanceRepository walletBalanceRepository) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.userService = userService;
        this.coinGekoClient = coinGekoClient;
        this.walletBalanceRepository = walletBalanceRepository;
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

        BigDecimal balance = null;
        for(WalletBalance walletBalance: wallet.getBalances()){
            if(Objects.isNull(balance)){
                balance = walletBalance.getBalance();
            }
            balance.add(walletBalance.getBalance());
        }
        return walletMapper.mapToResponseDto(wallet, userResponse.id());
    }

    @Transactional
    @Override
    public WalletResponse updateWallet(String id, UpdateWalletRequest updateWalletRequest) {
        Wallet wallet = walletRepository.findByWalletId(id)
                .orElseThrow(() -> new MyServiceException("Wallet not found", ErrorCode.BUSINESS_ERROR));
        Optional<WalletBalance> walletBalanceOptional = wallet.getBalances()
                .stream()
                .filter(balance -> balance.getCurrency() == updateWalletRequest.getCurrency())
                .findAny();

        WalletBalance walletBalance = getTotalWalletBalance(updateWalletRequest, wallet, walletBalanceOptional);
        walletBalanceRepository.save(walletBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        UserResponse userResponse = userService.getUserByInternalId(wallet.getUserId());
        WalletResponse walletResponse = walletMapper.mapToResponseDto(walletRepository.save(wallet), userResponse.id());
        Map<String, BigDecimal> balances = new HashMap<>();
        for(WalletBalance b : wallet.getBalances()){
            balances.put(b.getCurrency().name(), b.getBalance());
            calculateWalletValue(b.getBalance(), b.getCurrency());
            walletResponse.setBalance(walletResponse.getBalance().add(calculateWalletValue(b.getBalance(), b.getCurrency())));
        }
        walletResponse.setBalances(balances);
        return walletResponse;
    }

    private static WalletBalance getTotalWalletBalance(UpdateWalletRequest updateWalletRequest, Wallet wallet, Optional<WalletBalance> walletBalanceOptional) {
        WalletBalance walletBalance = null;
        if(walletBalanceOptional.isPresent()){
            walletBalance = walletBalanceOptional.get();
            walletBalance.setBalance(walletBalance.getBalance().add(updateWalletRequest.getBalance()));
            walletBalance.setUpdatedAt(LocalDateTime.now());
        }else{
            walletBalance =
                    new WalletBalance(wallet, updateWalletRequest.getCurrency(), updateWalletRequest.getBalance());
        }
        return walletBalance;
    }

    @Transactional
    @Override
    public void deleteWallet(String id) {
        Wallet wallet = walletRepository.findByWalletId(id)
                .orElseThrow(() -> new MyServiceException("Wallet not found", ErrorCode.BUSINESS_ERROR));
        wallet.setDeletedAt(LocalDateTime.now());
        walletRepository.save(wallet);
    }

    private BigDecimal calculateWalletValue(BigDecimal value, Currency currency){
        BigDecimal coinPrice = coinGekoClient.fetchPrice(currency.name().toLowerCase());
        return value.multiply(coinPrice);
    }
}
