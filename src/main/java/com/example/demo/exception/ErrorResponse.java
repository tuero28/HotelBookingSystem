package com.example.demo.exception;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder
public class ErrorResponse {
    private int status;
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;
}