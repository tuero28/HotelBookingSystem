
package com.example.demo.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getStatus()).body(ErrorResponse.builder()
                .status(ex.getStatus().value())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .status(400).errorCode("VALIDATION_ERROR").message(message)
                .timestamp(LocalDateTime.now()).build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) throws Exception {

        if (ex instanceof AccessDeniedException ||
                ex instanceof AuthenticationException ||
                ex instanceof org.springframework.security.core.AuthenticationException ||
                ex instanceof org.springframework.web.servlet.resource.NoResourceFoundException ||
                ex instanceof jakarta.servlet.ServletException) {
            throw ex;
        }

        return ResponseEntity.status(500).body(ErrorResponse.builder()
                .status(500).errorCode("INTERNAL_ERROR").message("An unexpected error occurred: " + ex.getMessage())
                .timestamp(LocalDateTime.now()).build());
    }
}
