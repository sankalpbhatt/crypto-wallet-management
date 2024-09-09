package com.crypto.exception.model;

public enum ErrorCode {
    GENERAL("E0001", "General Error"),
    BUSINESS_ERROR("E0002", "Business Error"),
    PERMISSION("E0003", "Permission Issue");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
