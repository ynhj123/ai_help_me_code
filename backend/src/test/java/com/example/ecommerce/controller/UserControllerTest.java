package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.dto.UserQueryRequest;
import com.example.ecommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * UserController的测试类
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void testGetProfile() throws Exception {
        // 准备测试数据
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setNickname("Test User");

        // 配置mock行为
        when(userService.getProfile()).thenReturn(userDTO);

        // 执行被测方法
        mockMvc.perform(get("/api/users/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.nickname").value("Test User"));

        // 验证结果
        verify(userService).getProfile();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserList() throws Exception {
        // 准备测试数据
        List<UserDTO> userList = new ArrayList<>();
        UserDTO user1 = new UserDTO();
        user1.setId(1L);
        user1.setUsername("user1");
        UserDTO user2 = new UserDTO();
        user2.setId(2L);
        user2.setUsername("user2");
        userList.add(user1);
        userList.add(user2);

        // 配置mock行为
        when(userService.getUserList(1, 10)).thenReturn(userList);

        // 执行被测方法
        mockMvc.perform(get("/api/users")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("user1"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("user2"));

        // 验证结果
        verify(userService).getUserList(1, 10);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetUserList_Forbidden() throws Exception {
        // 执行被测方法并验证权限
        mockMvc.perform(get("/api/users")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // 验证结果
        verify(userService, never()).getUserList(anyInt(), anyInt());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSearchUsers() throws Exception {
        // 准备测试数据
        List<UserDTO> userList = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("testuser");
        userList.add(user);

        // 配置mock行为
        when(userService.getUserList("test", null, null, null, null, null, null, null, null, null, 1, 10)).thenReturn(userList);

        // 执行被测方法
        mockMvc.perform(get("/api/users/search")
                .param("username", "test")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("testuser"));

        // 验证结果
        verify(userService).getUserList("test", null, null, null, null, null, null, null, null, null, 1, 10);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserCount() throws Exception {
        // 配置mock行为
        when(userService.getUserCount()).thenReturn(10);

        // 执行被测方法
        mockMvc.perform(get("/api/users/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(10));

        // 验证结果
        verify(userService).getUserCount();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testSearchUserCount() throws Exception {
        // 配置mock行为
        when(userService.getUserCount("test", "example.com", null, null, null, null, null, null)).thenReturn(5);

        // 执行被测方法
        mockMvc.perform(get("/api/users/count/search")
                .param("username", "test")
                .param("email", "example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(5));

        // 验证结果
        verify(userService).getUserCount("test", "example.com", null, null, null, null, null, null);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testExistsByUsername() throws Exception {
        // 配置mock行为
        when(userService.existsByUsername("testuser")).thenReturn(true);

        // 执行被测方法
        mockMvc.perform(get("/api/users/exists/username")
                .param("username", "testuser")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        // 验证结果
        verify(userService).existsByUsername("testuser");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testExistsByEmail() throws Exception {
        // 配置mock行为
        when(userService.existsByEmail("test@example.com")).thenReturn(false);

        // 执行被测方法
        mockMvc.perform(get("/api/users/exists/email")
                .param("email", "test@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));

        // 验证结果
        verify(userService).existsByEmail("test@example.com");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testExistsByPhone() throws Exception {
        // 配置mock行为
        when(userService.existsByPhone("13800138000")).thenReturn(true);

        // 执行被测方法
        mockMvc.perform(get("/api/users/exists/phone")
                .param("phone", "13800138000")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        // 验证结果
        verify(userService).existsByPhone("13800138000");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetUserById() throws Exception {
        // 准备测试数据
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");

        // 配置mock行为
        when(userService.getUser(userId)).thenReturn(userDTO);

        // 执行被测方法
        mockMvc.perform(get("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        // 验证结果
        verify(userService).getUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateUser() throws Exception {
        // 准备测试数据
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setEmail("new@example.com");
        userDTO.setPassword("password");

        UserDTO createdUserDTO = new UserDTO();
        createdUserDTO.setId(1L);
        createdUserDTO.setUsername("newuser");
        createdUserDTO.setEmail("new@example.com");

        // 配置mock行为
        when(userService.createUser(any(UserDTO.class))).thenReturn(createdUserDTO);

        // 执行被测方法
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("newuser"))
                .andExpect(jsonPath("$.data.email").value("new@example.com"))
                .andExpect(header().string("Location", "/api/users/1"));

        // 验证结果
        verify(userService).createUser(any(UserDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateUser() throws Exception {
        // 准备测试数据
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("updateduser");
        userDTO.setEmail("updated@example.com");

        UserDTO updatedUserDTO = new UserDTO();
        updatedUserDTO.setId(userId);
        updatedUserDTO.setUsername("updateduser");
        updatedUserDTO.setEmail("updated@example.com");

        // 配置mock行为
        when(userService.updateUser(eq(userId), any(UserDTO.class))).thenReturn(updatedUserDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(userId))
                .andExpect(jsonPath("$.data.username").value("updateduser"))
                .andExpect(jsonPath("$.data.email").value("updated@example.com"));

        // 验证结果
        verify(userService).updateUser(eq(userId), any(UserDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        // 执行被测方法
        mockMvc.perform(delete("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 验证结果
        verify(userService).deleteUser(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindRecentUsers() throws Exception {
        // 准备测试数据
        int limit = 5;
        List<UserDTO> userList = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("recentuser");
        userList.add(user);

        // 配置mock行为
        when(userService.findRecentUsers(limit)).thenReturn(userList);

        // 执行被测方法
        mockMvc.perform(get("/api/users/recent")
                .param("limit", String.valueOf(limit))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("recentuser"));

        // 验证结果
        verify(userService).findRecentUsers(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindActiveUsers() throws Exception {
        // 准备测试数据
        int limit = 10;
        List<UserDTO> userList = new ArrayList<>();
        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setUsername("activeuser");
        userList.add(user);

        // 配置mock行为
        when(userService.findActiveUsers(limit)).thenReturn(userList);

        // 执行被测方法
        mockMvc.perform(get("/api/users/active")
                .param("limit", String.valueOf(limit))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("activeuser"));

        // 验证结果
        verify(userService).findActiveUsers(limit);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindRoleDistribution() throws Exception {
        // 执行被测方法
        mockMvc.perform(get("/api/users/statistics/role-distribution")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        // 验证结果
        verify(userService).findRoleDistribution();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindStatusDistribution() throws Exception {
        // 执行被测方法
        mockMvc.perform(get("/api/users/statistics/status-distribution")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        // 验证结果
        verify(userService).findStatusDistribution();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindRegistrationTrendByDay() throws Exception {
        // 执行被测方法
        mockMvc.perform(get("/api/users/statistics/registration-trend/day")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        // 验证结果
        verify(userService).findRegistrationTrendByDay();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindRegistrationTrendByMonth() throws Exception {
        // 执行被测方法
        mockMvc.perform(get("/api/users/statistics/registration-trend/month")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        // 验证结果
        verify(userService).findRegistrationTrendByMonth();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindRegistrationTrendByYear() throws Exception {
        // 执行被测方法
        mockMvc.perform(get("/api/users/statistics/registration-trend/year")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        // 验证结果
        verify(userService).findRegistrationTrendByYear();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testFindUserActivityStats() throws Exception {
        // 执行被测方法
        mockMvc.perform(get("/api/users/statistics/activity")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());

        // 验证结果
        verify(userService).findUserActivityStats();
    }
}