package com.crypto.transaction.service.impl;

import com.crypto.common.entity.SequenceType;
import com.crypto.common.repository.SequenceGeneratorRepository;
import com.crypto.common.service.SequenceGeneratorServiceImpl;
import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import com.crypto.transaction.dto.request.CreateTransactionRequest;
import com.crypto.transaction.dto.response.TransactionResponse;
import com.crypto.transaction.entity.Transaction;
import com.crypto.transaction.entity.TransactionStatus;
import com.crypto.transaction.entity.TransactionType;
import com.crypto.transaction.mapper.TransactionMapper;
import com.crypto.transaction.repository.TransactionRepository;
import com.crypto.transaction.service.TransactionService;
import com.crypto.util.CryptoUtils;
import com.crypto.wallet.dto.request.UpdateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class TransactionServiceImpl extends SequenceGeneratorServiceImpl implements TransactionService {

    @Value("${private.key.phrase}")
    private String passPhrase;

    private final TransactionRepository transactionRepository;
    private final SequenceGeneratorRepository sequenceGeneratorRepository;
    private final WalletService walletService;
    protected final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  SequenceGeneratorRepository sequenceGeneratorRepository,
                                  WalletService walletService,
                                  TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.sequenceGeneratorRepository = sequenceGeneratorRepository;
        this.walletService = walletService;
        this.transactionMapper = transactionMapper;
    }

    @Override
    @Transactional
    public CompletableFuture<TransactionResponse> createTransaction(CreateTransactionRequest createTransactionRequest) {
        WalletResponse walletResponse = walletService.getWalletById(createTransactionRequest.getWalletId());
        validateRequest(createTransactionRequest, walletResponse);
        UpdateWalletRequest updateWalletRequest = createUpdateWalletRequest(createTransactionRequest);
        walletService.updateWallet(walletResponse.getId(), updateWalletRequest);
        Transaction transaction = transactionMapper
                .mapToEntity(createTransactionRequest, walletResponse.getInternalId());
        String transactionId = SequenceType.TRANSACTION.getPrefix() +
                getNextSequenceValue(SequenceType.TRANSACTION, sequenceGeneratorRepository);
        transaction.setTransactionId(transactionId);
        transaction = transactionRepository.save(transaction);
        Transaction finalTransaction = transaction;
        return getTransactionResponseCompletableFuture(walletResponse, finalTransaction);

    }

    private CompletableFuture<TransactionResponse> getTransactionResponseCompletableFuture(WalletResponse walletResponse, Transaction finalTransaction) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                finalTransaction.setStatus(TransactionStatus.CONFIRMED);
                finalTransaction.setUpdatedAt(LocalDateTime.now());
                transactionRepository.save(finalTransaction);
                TransactionResponse transactionResponse = transactionMapper.mapToResponseDto(finalTransaction);
                transactionResponse.setWalletId(walletResponse.getId());
                return transactionResponse;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                finalTransaction.setStatus(TransactionStatus.FAILED);
                finalTransaction.setUpdatedAt(LocalDateTime.now());
                transactionRepository.save(finalTransaction);
                TransactionResponse transactionResponse = transactionMapper.mapToResponseDto(finalTransaction);
                transactionResponse.setWalletId(walletResponse.getId());
                return transactionResponse;
            }
        });
    }

    private void validateRequest(CreateTransactionRequest createTransactionRequest,
                                 WalletResponse walletResponse) {
        int compareBalance = createTransactionRequest.getAmount().compareTo(walletResponse.getBalance());
        if (createTransactionRequest.getType() == TransactionType.SEND && compareBalance > 0) {
            if (compareBalance > 0) {
                throw new MyServiceException("Insufficient Funds", ErrorCode.BUSINESS_ERROR);
            }
            String privateKey = CryptoUtils.decryptPrivateKey(walletResponse.getEncryptedKey(), passPhrase);
            CryptoUtils.decryptSignature(createTransactionRequest.getSignature(), CryptoUtils.getPrivateKeyFromString(privateKey));
        }
    }

    private static UpdateWalletRequest createUpdateWalletRequest(CreateTransactionRequest createTransactionRequest) {
        UpdateWalletRequest updateWalletRequest = new UpdateWalletRequest();
        updateWalletRequest.setBalance(createTransactionRequest.getAmount());
        if (createTransactionRequest.getType() == TransactionType.SEND) {
            updateWalletRequest.setOperation(UpdateWalletRequest.Operation.SUBTRACT);
        } else if (createTransactionRequest.getType() == TransactionType.RECEIVE) {
            updateWalletRequest.setOperation(UpdateWalletRequest.Operation.ADD);
        }
        updateWalletRequest.setCurrency(createTransactionRequest.getCurrency());
        return updateWalletRequest;
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(String id) {
        Transaction transaction = transactionRepository.findByTransactionId(id)
                .orElseThrow(() -> new NoSuchElementException("Wallet not found"));
        return transactionMapper.mapToResponseDto(transaction);
    }
}
