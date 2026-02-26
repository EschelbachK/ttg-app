package com.traintogain.backend.api;

public class ApiResponse<T> {

    private boolean success;
    private T data;

    private ApiResponse(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }
}