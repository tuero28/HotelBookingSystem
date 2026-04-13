package com.example.demo.util;

import lombok.*;

@Getter @Setter @Builder @AllArgsConstructor @NoArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().status(200).message("Success").data(data).build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder().status(200).message(message).data(data).build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder().status(201).message("Created successfully").data(data).build();
    }

    public static ApiResponse<Void> noContent(String message) {
        return ApiResponse.<Void>builder().status(200).message(message).build();
    }
}