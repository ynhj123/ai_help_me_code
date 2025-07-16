package com.admin.modules.auth.controller;

import com.admin.common.security.service.UserDetailsImpl;
import com.admin.modules.auth.dto.UserDto;
import com.admin.modules.auth.dto.UserUpdateRequest;
import com.admin.modules.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "用户管理相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "获取用户列表", description = "分页获取所有用户列表")
    @ApiResponse(responseCode = "200", description = "获取成功")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息")
    @ApiResponse(responseCode = "200", description = "获取成功")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    @ApiResponse(responseCode = "200", description = "获取成功")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserDto user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "更新用户信息", description = "更新指定用户的详细信息")
    @ApiResponse(responseCode = "200", description = "更新成功")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserDto updatedUser = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    @Operation(summary = "删除用户", description = "软删除指定用户")
    @ApiResponse(responseCode = "204", description = "删除成功")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "冻结用户", description = "冻结指定用户账户")
    @ApiResponse(responseCode = "200", description = "冻结成功")
    public ResponseEntity<UserDto> freezeUser(@PathVariable Long id) {
        UserDto user = userService.freezeUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/unfreeze")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "解冻用户", description = "解冻指定用户账户")
    @ApiResponse(responseCode = "200", description = "解冻成功")
    public ResponseEntity<UserDto> unfreezeUser(@PathVariable Long id) {
        UserDto user = userService.unfreezeUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('user:update')")
    @Operation(summary = "重置密码", description = "重置指定用户的密码")
    @ApiResponse(responseCode = "200", description = "重置成功")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
}