package com.admin.modules.auth.service;

import com.admin.common.exception.ResourceNotFoundException;
import com.admin.modules.auth.dto.UserDto;
import com.admin.modules.auth.dto.UserUpdateRequest;
import com.admin.modules.auth.entity.Role;
import com.admin.modules.auth.entity.User;
import com.admin.modules.auth.enums.UserStatus;
import com.admin.modules.auth.enums.UserType;
import com.admin.modules.auth.repository.RoleRepository;
import com.admin.modules.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Role testRole;

    @BeforeEach
    void setUp() {
        testRole = new Role();
        testRole.setId(1L);
        testRole.setName("USER");
        testRole.setCode("USER");

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("encodedPassword");
        testUser.setStatus(UserStatus.ACTIVE);
        testUser.setUserType(UserType.USER);
        testUser.setRoles(new HashSet<>(Arrays.asList(testRole)));
    }

    @Test
    void getAllUsers_ShouldReturnPageOfUsers() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<User> users = Arrays.asList(testUser);
        Page<User> userPage = new PageImpl<>(users, pageable, users.size());

        when(userRepository.findAll(pageable)).thenReturn(userPage);

        // When
        Page<UserDto> result = userService.getAllUsers(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("testuser", result.getContent().get(0).getUsername());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDto result = userService.getUserById(1L);

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
    }

    @Test
    void updateUser_ShouldUpdateUserSuccessfully() {
        // Given
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setPhone("1234567890");
        updateRequest.setRoleIds(new HashSet<>(Arrays.asList(1L)));

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("updateduser")).thenReturn(false);
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(roleRepository.findAllById(anySet())).thenReturn(Arrays.asList(testRole));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        UserDto result = userService.updateUser(1L, updateRequest);

        // Then
        assertNotNull(result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenUsernameExists() {
        // Given
        UserUpdateRequest updateRequest = new UserUpdateRequest();
        updateRequest.setUsername("existinguser");
        updateRequest.setEmail("updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, updateRequest);
        });
    }

    @Test
    void deleteUser_ShouldSetStatusToDeleted() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(1L);

        // Then
        assertEquals(UserStatus.DELETED, testUser.getStatus());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenDeletingSuperAdmin() {
        // Given
        testUser.setUserType(UserType.SUPER_ADMIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.deleteUser(1L);
        });
    }

    @Test
    void freezeUser_ShouldSetStatusToFrozen() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDto result = userService.freezeUser(1L);

        // Then
        assertNotNull(result);
        assertEquals(UserStatus.FROZEN, testUser.getStatus());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void freezeUser_ShouldThrowException_WhenAlreadyFrozen() {
        // Given
        testUser.setStatus(UserStatus.FROZEN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            userService.freezeUser(1L);
        });
    }

    @Test
    void unfreezeUser_ShouldSetStatusToActive() {
        // Given
        testUser.setStatus(UserStatus.FROZEN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // When
        UserDto result = userService.unfreezeUser(1L);

        // Then
        assertNotNull(result);
        assertEquals(UserStatus.ACTIVE, testUser.getStatus());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void resetPassword_ShouldUpdatePasswordHash() {
        // Given
        String newPassword = "newPassword123";
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // When
        userService.resetPassword(1L, newPassword);

        // Then
        assertEquals("encodedNewPassword", testUser.getPasswordHash());
        verify(userRepository, times(1)).save(testUser);
    }
}