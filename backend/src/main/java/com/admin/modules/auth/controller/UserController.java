package com.admin.modules.auth.controller;

import com.admin.common.security.service.UserDetailsImpl;
import com.admin.modules.auth.dto.UserDto;
import com.admin.modules.auth.dto.UserUpdateRequest;
import com.admin.modules.auth.service.UserService;
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
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        UserDto user = userService.getUserByUsername(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest updateRequest) {
        UserDto updatedUser = userService.updateUser(id, updateRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<UserDto> freezeUser(@PathVariable Long id) {
        UserDto user = userService.freezeUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/unfreeze")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<UserDto> unfreezeUser(@PathVariable Long id) {
        UserDto user = userService.unfreezeUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<Void> resetPassword(
            @PathVariable Long id,
            @RequestParam String newPassword) {
        userService.resetPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
}