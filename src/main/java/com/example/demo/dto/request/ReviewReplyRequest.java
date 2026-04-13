package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
public class ReviewReplyRequest {
    @NotBlank private String reply;
}