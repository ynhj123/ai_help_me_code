package com.admin.modules.auth.dto;

import lombok.Data;

@Data
public class SignupResponse {
    private String message;
    private Long userId;
    private String username;
}