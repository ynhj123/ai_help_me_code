package com.admin.e2e;

import com.admin.modules.auth.dto.LoginRequest;
import com.admin.modules.auth.dto.SignupRequest;
import com.admin.modules.auth.dto.UserUpdateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserManagementE2ETest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void completeUserLifecycle_ShouldWorkCorrectly() throws Exception {
        // 1. 注册用户
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername("e2euser");
        signupRequest.setEmail("e2e@example.com");
        signupRequest.setPassword("password123");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));

        // 2. 用户登录
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("e2euser");
        loginRequest.setPassword("password123");

        String loginResponse = mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();

        String token = objectMapper.readTree(loginResponse).get("token").asText();

        // 3. 获取用户列表（需要管理员权限）
        mockMvc.perform(get("/api/users")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(greaterThan(0))));

        // 4. 获取当前用户信息
        mockMvc.perform(get("/api/users/profile")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("e2euser"))
                .andExpect(jsonPath("$.email").value("e2e@example.com"));

        // 5. 更新用户信息
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("e2euser_updated");
        updateRequest.setEmail("e2e_updated@example.com");
        updateRequest.setPhone("1234567890");

        mockMvc.perform(put("/api/users/2") // 假设新创建的用户ID为2
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("e2euser_updated"))
                .andExpect(jsonPath("$.email").value("e2e_updated@example.com"));

        // 6. 冻结用户
        mockMvc.perform(post("/api/users/2/freeze")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FROZEN"));

        // 7. 解冻用户
        mockMvc.perform(post("/api/users/2/unfreeze")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"));

        // 8. 重置密码
        mockMvc.perform(post("/api/users/2/reset-password")
                .header("Authorization", "Bearer " + token)
                .param("newPassword", "newpassword123"))
                .andExpect(status().isOk());
    }

    @Test
    void userRegistration_ShouldHandleValidationErrors() throws Exception {
        // 测试无效邮箱格式
        SignupRequest invalidRequest = new SignupRequest();
        invalidRequest.setUsername("user");
        invalidRequest.setEmail("invalid-email");
        invalidRequest.setPassword("123");

        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void userLogin_ShouldHandleInvalidCredentials() throws Exception {
        LoginRequest invalidLogin = new LoginRequest();
        invalidLogin.setUsername("nonexistent");
        invalidLogin.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isUnauthorized());
    }
}