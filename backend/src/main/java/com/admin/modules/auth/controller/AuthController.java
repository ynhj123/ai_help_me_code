package com.admin.modules.auth.controller;

import com.admin.modules.auth.dto.LoginRequest;
import com.admin.modules.auth.dto.LoginResponse;
import com.admin.modules.auth.dto.SignupRequest;
import com.admin.modules.auth.dto.SignupResponse;
import com.admin.modules.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, 
                                            HttpServletRequest request) {
        String ip = getClientIp(request);
        LoginResponse loginResponse = authService.authenticateUser(loginRequest, ip);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        SignupResponse signupResponse = authService.registerUser(signUpRequest);
        return ResponseEntity.ok(signupResponse);
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}