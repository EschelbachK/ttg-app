package com.traintogain.backend.api;

public class ApiResponse<T> {

    private final boolean success;
    private final T data;

    private ApiResponse(T data) {
        this.success = true;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }

    public boolean isSuccess() { return success; }
    public T getData() { return data; }
}