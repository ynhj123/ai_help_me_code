package com.admin.modules.auth.dto;

import com.admin.modules.auth.enums.UserStatus;
import com.admin.modules.auth.enums.UserType;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String avatarUrl;
    private UserStatus status;
    private UserType userType;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private Set<String> roles;

    public UserDto() {}

    public UserDto(Long id, String username, String email, String phone, String avatarUrl,
                   UserStatus status, UserType userType, LocalDateTime lastLoginAt,
                   LocalDateTime createdAt, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.userType = userType;
        this.lastLoginAt = lastLoginAt;
        this.createdAt = createdAt;
        this.roles = roles;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}