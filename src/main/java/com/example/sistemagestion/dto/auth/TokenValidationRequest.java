package com.example.sistemagestion.dto.auth;

import lombok.Data;

@Data
public class TokenValidationRequest {
    private String access_token;
    private String refresh_token;
}