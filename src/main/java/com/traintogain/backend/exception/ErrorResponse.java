package com.traintogain.backend.exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private final boolean success = false;
    private final String message;
    private final String code;
    private final int status;
    private final LocalDateTime timestamp;

    public ErrorResponse(String message, String code, int status) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public String getCode() { return code; }
    public int getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
}