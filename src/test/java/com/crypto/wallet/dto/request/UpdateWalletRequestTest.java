package com.crypto.wallet.dto.request;

import com.crypto.wallet.dto.Currency;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateWalletRequestTest {

    private Validator validator;
    private UpdateWalletRequest updateWalletRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    static Stream<UpdateWalletRequest> provideInvalidRequests() {
        return Stream.of(
                new UpdateWalletRequest(null, BigDecimal.valueOf(100), Currency.BITCOIN),
                new UpdateWalletRequest(UpdateWalletRequest.Operation.ADD, null, Currency.BITCOIN),
                new UpdateWalletRequest(UpdateWalletRequest.Operation.ADD, BigDecimal.valueOf(100))
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRequests")
    void shouldFailForMissingMandatoryFields(UpdateWalletRequest request) {
        Set<ConstraintViolation<UpdateWalletRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }

    @Test
    void shouldFailForAllMissingMandatoryRequest() {
        updateWalletRequest = new UpdateWalletRequest();
        Set<ConstraintViolation<UpdateWalletRequest>> violations = validator.validate(updateWalletRequest);
        assertThat(violations).hasSize(3);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("must not be null");
    }
}