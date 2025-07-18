package com.admin.modules.auth.dto;

import lombok.Data;
import java.util.List;

@Data
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
}