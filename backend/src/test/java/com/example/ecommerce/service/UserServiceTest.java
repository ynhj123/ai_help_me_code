package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.UserRole;
import com.example.ecommerce.enums.UserStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.service.impl.UserServiceImpl;
import com.example.ecommerce.util.JwtUtil;
import com.example.ecommerce.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService的测试类
 */
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 清空SecurityContext，避免测试之间的影响
        SecurityContextHolder.clearContext();
    }

    @Test
    void testLogin_Success() {
        // 准备测试数据
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        UserDetails userDetails = mock(UserDetails.class);

        // 配置mock行为
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(userDetails);
        when(jwtUtil.generateToken(userDetails)).thenReturn("jwt-token");

        // 执行被测方法
        String token = userService.login(loginRequest);

        // 验证结果
        assertEquals("jwt-token", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername("testuser");
        verify(jwtUtil).generateToken(userDetails);
    }

    @Test
    void testRegister_Success() {
        // 准备测试数据
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPhone("1234567890");
        registerRequest.setPassword("password");

        // 配置mock行为
        when(userMapper.findByUsername("newuser")).thenReturn(null);
        when(userMapper.findByEmail("newuser@example.com")).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // 执行被测方法
        userService.register(registerRequest);

        // 验证结果
        verify(userMapper).findByUsername("newuser");
        verify(userMapper).findByEmail("newuser@example.com");
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void testRegister_UsernameExists() {
        // 准备测试数据
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("existinguser");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPhone("1234567890");
        registerRequest.setPassword("password");

        // 配置mock行为
        User existingUser = new User();
        when(userMapper.findByUsername("existinguser")).thenReturn(existingUser);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register(registerRequest);
        });

        // 验证结果
        assertEquals(400, exception.getCode());
        assertEquals("用户名已存在", exception.getMessage());
        verify(userMapper).findByUsername("existinguser");
        verify(userMapper, never()).findByEmail(anyString());
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testRegister_EmailExists() {
        // 准备测试数据
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("existing@example.com");
        registerRequest.setPhone("1234567890");
        registerRequest.setPassword("password");

        // 配置mock行为
        when(userMapper.findByUsername("newuser")).thenReturn(null);
        User existingUser = new User();
        when(userMapper.findByEmail("existing@example.com")).thenReturn(existingUser);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.register(registerRequest);
        });

        // 验证结果
        assertEquals(400, exception.getCode());
        assertEquals("邮箱已存在", exception.getMessage());
        verify(userMapper).findByUsername("newuser");
        verify(userMapper).findByEmail("existing@example.com");
        verify(userMapper, never()).insert(any(User.class));
    }

    @Test
    void testGetProfile_Success() {
        // 准备测试数据
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        // 配置mock行为
        when(userMapper.findByUsername("testuser")).thenReturn(user);

        // 执行被测方法
        UserDTO userDTO = userService.getProfile();

        // 验证结果
        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("testuser@example.com", userDTO.getEmail());
        verify(userMapper).findByUsername("testuser");
    }

    @Test
    void testGetProfile_UserNotFound() {
        // 准备测试数据
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("notexistuser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 配置mock行为
        when(userMapper.findByUsername("notexistuser")).thenReturn(null);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.getProfile();
        });

        // 验证结果
        assertEquals(404, exception.getCode());
        assertEquals("用户不存在", exception.getMessage());
        verify(userMapper).findByUsername("notexistuser");
    }

    @Test
    void testGetUserList_Success() {
        // 准备测试数据
        int page = 1;
        int size = 10;
        int offset = (page - 1) * size;

        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        userList.add(user1);
        userList.add(user2);

        // 配置mock行为
        when(userMapper.selectAll(offset, size)).thenReturn(userList);

        // 执行被测方法
        List<UserDTO> userDTOList = userService.getUserList(page, size);

        // 验证结果
        assertNotNull(userDTOList);
        assertEquals(2, userDTOList.size());
        assertEquals(1L, userDTOList.get(0).getId());
        assertEquals("user1", userDTOList.get(0).getUsername());
        assertEquals(2L, userDTOList.get(1).getId());
        assertEquals("user2", userDTOList.get(1).getUsername());
        verify(userMapper).selectAll(offset, size);
    }

    @Test
    void testGetUser_Success() {
        // 准备测试数据
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");

        // 配置mock行为
        when(userMapper.selectById(userId)).thenReturn(user);

        // 执行被测方法
        UserDTO userDTO = userService.getUser(userId);

        // 验证结果
        assertNotNull(userDTO);
        assertEquals(userId, userDTO.getId());
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("testuser@example.com", userDTO.getEmail());
        verify(userMapper).selectById(userId);
    }

    @Test
    void testGetUser_NotFound() {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(userMapper.selectById(userId)).thenReturn(null);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.getUser(userId);
        });

        // 验证结果
        assertEquals(404, exception.getCode());
        assertEquals("用户不存在", exception.getMessage());
        verify(userMapper).selectById(userId);
    }

    @Test
    void testCreateUser_Success() {
        // 准备测试数据
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setEmail("newuser@example.com");
        userDTO.setPassword("password");

        User user = new User();
        user.setId(1L);
        user.setUsername("newuser");
        user.setEmail("newuser@example.com");

        // 配置mock行为
        when(userMapper.findByUsername("newuser")).thenReturn(null);
        when(userMapper.findByEmail("newuser@example.com")).thenReturn(null);
        when(userMapper.insert(any(User.class))).thenReturn(1);
        when(userMapper.selectById(1L)).thenReturn(user);

        // 执行被测方法
        UserDTO createdUserDTO = userService.createUser(userDTO);

        // 验证结果
        assertNotNull(createdUserDTO);
        assertEquals(1L, createdUserDTO.getId());
        assertEquals("newuser", createdUserDTO.getUsername());
        assertEquals("newuser@example.com", createdUserDTO.getEmail());
        verify(userMapper).findByUsername("newuser");
        verify(userMapper).findByEmail("newuser@example.com");
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void testUpdateUser_Success() {
        // 准备测试数据
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updateduser");
        userDTO.setEmail("updateduser@example.com");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setUsername("olduser");
        existingUser.setEmail("olduser@example.com");

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updateduser@example.com");

        // 配置mock行为
        when(userMapper.selectById(userId)).thenReturn(existingUser);
        when(userMapper.findByUsername("updateduser")).thenReturn(null);
        when(userMapper.findByEmail("updateduser@example.com")).thenReturn(null);
        when(userMapper.update(any(User.class))).thenReturn(1);
        when(userMapper.selectById(userId)).thenReturn(updatedUser);

        // 执行被测方法
        UserDTO resultDTO = userService.updateUser(userId, userDTO);

        // 验证结果
        assertNotNull(resultDTO);
        assertEquals(userId, resultDTO.getId());
        assertEquals("updateduser", resultDTO.getUsername());
        assertEquals("updateduser@example.com", resultDTO.getEmail());
        verify(userMapper).selectById(userId);
        verify(userMapper).findByUsername("updateduser");
        verify(userMapper).findByEmail("updateduser@example.com");
        verify(userMapper).update(any(User.class));
    }

    @Test
    void testDeleteUser_Success() {
        // 准备测试数据
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        // 配置mock行为
        when(userMapper.selectById(userId)).thenReturn(user);
        when(userMapper.deleteById(userId)).thenReturn(1);

        // 执行被测方法
        userService.deleteUser(userId);

        // 验证结果
        verify(userMapper).selectById(userId);
        verify(userMapper).deleteById(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(userMapper.selectById(userId)).thenReturn(null);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.deleteUser(userId);
        });

        // 验证结果
        assertEquals(404, exception.getCode());
        assertEquals("用户不存在", exception.getMessage());
        verify(userMapper).selectById(userId);
        verify(userMapper, never()).deleteById(anyLong());
    }
}