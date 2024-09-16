package com.crypto.transaction.dto.request;

import com.crypto.transaction.entity.TransactionType;
import com.crypto.wallet.dto.Currency;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CreateTransactionRequestTest {

    private Validator validator;

    private CreateTransactionRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRequestData")
    void whenFieldsAreInvalid_thenValidationFails(String walletId, TransactionType type, BigDecimal amount, Currency currency, int expectedViolations) {
        request = new CreateTransactionRequest();
        request.setWalletId(walletId);
        request.setType(type);
        request.setAmount(amount);
        request.setCurrency(currency);

        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);

        assertEquals(expectedViolations, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideValidRequestData")
    void whenAllFieldsAreValid_thenValidationSucceeds(CreateTransactionRequest request) {
        Set<ConstraintViolation<CreateTransactionRequest>> violations = validator.validate(request);
        assertEquals(0, violations.size());
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideInvalidRequestData() {
        return Stream.of(
                arguments(null, TransactionType.SEND, BigDecimal.valueOf(100), Currency.ETHEREUM, 1),
                arguments("W0001", null, BigDecimal.valueOf(100), Currency.ETHEREUM, 1),
                arguments("W0001", TransactionType.RECEIVE, null, Currency.ETHEREUM, 1),
                arguments("W0001", TransactionType.RECEIVE, BigDecimal.valueOf(100), null, 1),
                arguments(null, null, null, null, 4)
        );
    }

    private static Stream<CreateTransactionRequest> provideValidRequestData() {
        return Stream.of(
                createValidRequest("W0001", TransactionType.SEND, BigDecimal.valueOf(100), Currency.BITCOIN),
                createValidRequest("W0002", TransactionType.RECEIVE, BigDecimal.valueOf(50), Currency.ETHEREUM)
        );
    }

    private static CreateTransactionRequest createValidRequest(String walletId, TransactionType type, BigDecimal amount, Currency currency) {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setWalletId(walletId);
        request.setType(type);
        request.setAmount(amount);
        request.setCurrency(currency);
        return request;
    }
}

