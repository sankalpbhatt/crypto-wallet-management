package com.crypto.exception;

import com.crypto.exception.model.ErrorCode;

public class MyServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    public MyServiceException(String message, ErrorCode errorCode) {
        super(String.format(errorCode.getMessage(), message));
        this.errorCode = errorCode;
    }

    public MyServiceException(String message, ErrorCode errorCode, Exception e) {
        super(String.format(errorCode.getMessage(), message));
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
