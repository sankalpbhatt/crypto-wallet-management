package com.crypto.wallet.mapper;

import com.crypto.wallet.dto.request.CreateWalletRequest;
import com.crypto.wallet.dto.response.WalletResponse;
import com.crypto.wallet.entity.Wallet;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public WalletResponse mapToResponseDto(Wallet wallet){
        return new WalletResponse();
    }

    public Wallet mapRequestToEntity(CreateWalletRequest request){
        return new Wallet();
    }
}
