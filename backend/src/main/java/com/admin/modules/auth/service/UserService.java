package com.admin.modules.auth.service;

import com.admin.common.exception.ResourceNotFoundException;
import com.admin.modules.auth.dto.UserDto;
import com.admin.modules.auth.dto.UserUpdateRequest;
import com.admin.modules.auth.entity.Role;
import com.admin.modules.auth.entity.User;
import com.admin.modules.auth.enums.UserStatus;
import com.admin.modules.auth.repository.RoleRepository;
import com.admin.modules.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::convertToDto);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertToDto(user);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        return convertToDto(user);
    }

    @Transactional
    public UserDto updateUser(Long id, UserUpdateRequest updateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // 检查用户名是否已被其他用户使用
        if (!user.getUsername().equals(updateRequest.getUsername()) &&
                userRepository.existsByUsername(updateRequest.getUsername())) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        // 检查邮箱是否已被其他用户使用
        if (!user.getEmail().equals(updateRequest.getEmail()) &&
                userRepository.existsByEmail(updateRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use!");
        }

        user.setUsername(updateRequest.getUsername());
        user.setEmail(updateRequest.getEmail());
        user.setPhone(updateRequest.getPhone());
        user.setAvatarUrl(updateRequest.getAvatarUrl());

        // 更新角色
        if (updateRequest.getRoleIds() != null && !updateRequest.getRoleIds().isEmpty()) {
            Set<Role> roles = roleRepository.findAllById(updateRequest.getRoleIds())
                    .stream()
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        // 不允许删除超级管理员
        if (user.getUserType().name().equals("SUPER_ADMIN")) {
            throw new IllegalArgumentException("Cannot delete super admin user");
        }

        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    @Transactional
    public UserDto freezeUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (user.getStatus() == UserStatus.FROZEN) {
            throw new IllegalArgumentException("User is already frozen");
        }

        user.setStatus(UserStatus.FROZEN);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Transactional
    public UserDto unfreezeUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new IllegalArgumentException("User is already active");
        }

        user.setStatus(UserStatus.ACTIVE);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Transactional
    public void resetPassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setStatus(user.getStatus());
        dto.setUserType(user.getUserType());
        dto.setLastLoginAt(user.getLastLoginAt());
        dto.setCreatedAt(user.getCreatedAt());

        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());
        dto.setRoles(roleNames);

        return dto;
    }
}