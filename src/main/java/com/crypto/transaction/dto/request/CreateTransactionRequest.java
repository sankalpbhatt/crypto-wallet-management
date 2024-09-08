package com.crypto.transaction.dto.request;

import com.crypto.wallet.dto.Currency;
import com.crypto.wallet.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Create request for Transaction")
public class CreateTransactionRequest {

    @NotNull
    @Schema(description = "Wallet id associated to transaction", example = "W0001")
    private String walletId;

    @NotNull
    @Schema(description = "Wallet id associated to transaction")
    private TransactionType type;

    @NotNull
    @Schema(description = "Wallet id associated to transaction", example = "W0001")
    private BigDecimal amount;

    @Schema(description = "Id of external transaction")
    private String externalReferenceId;

    @NotNull
    private Currency currency;

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getExternalReferenceId() {
        return externalReferenceId;
    }

    public void setExternalReferenceId(String externalReferenceId) {
        this.externalReferenceId = externalReferenceId;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}