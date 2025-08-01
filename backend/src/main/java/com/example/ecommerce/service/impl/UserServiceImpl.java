package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.enums.UserRole;
import com.example.ecommerce.enums.UserStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.util.JwtUtil;
import com.example.ecommerce.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String login(LoginRequest loginRequest) {
        // 认证用户
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 设置认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成JWT token
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());
        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public void register(RegisterRequest registerRequest) {
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(registerRequest.getUsername());
        if (existingUser != null) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 检查邮箱是否已存在
        existingUser = userMapper.findByEmail(registerRequest.getEmail());
        if (existingUser != null) {
            throw new BusinessException(400, "邮箱已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(PasswordUtil.encodePassword(registerRequest.getPassword()));
        user.setNickname(registerRequest.getUsername());
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(UserRole.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 保存用户
        userMapper.insert(user);
    }

    @Override
    public UserDTO getProfile() {
        // 获取当前认证用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 查询用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 转换为DTO
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public List<UserDTO> getUserList(Integer page, Integer size) {
        // 查询用户列表
        List<User> userList = userMapper.selectAll((page - 1) * size, size);

        // 转换为DTO列表
        return userList.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getUserList(String username, String email, String phone, String nickname,
                                   Integer status, String role, java.time.LocalDateTime startTime,
                                   java.time.LocalDateTime endTime, String sortBy, String sortOrder,
                                   Integer page, Integer size) {
        // 查询用户列表
        List<User> userList = userMapper.selectAllWithConditions(
                (page - 1) * size, size, username, email, phone, nickname, status, role,
                startTime, endTime, sortBy, sortOrder
        );

        // 转换为DTO列表
        return userList.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer getUserCount() {
        return userMapper.countAll();
    }

    @Override
    public Integer getUserCount(String username, String email, String phone, String nickname,
                              Integer status, String role, java.time.LocalDateTime startTime,
                              java.time.LocalDateTime endTime) {
        return userMapper.countAllWithConditions(username, email, phone, nickname, status, role, startTime, endTime);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    @Override
    public Boolean existsByPhone(String phone) {
        return userMapper.countByPhone(phone) > 0;
    }

    @Override
    public Boolean existsByUsernameExcludingId(String username, Long id) {
        return userMapper.countByUsernameExcludingId(username, id) > 0;
    }

    @Override
    public Boolean existsByEmailExcludingId(String email, Long id) {
        return userMapper.countByEmailExcludingId(email, id) > 0;
    }

    @Override
    public Boolean existsByPhoneExcludingId(String phone, Long id) {
        return userMapper.countByPhoneExcludingId(phone, id) > 0;
    }

    @Override
    public Integer countByStatus(Integer status) {
        return userMapper.countByStatus(status);
    }

    @Override
    public Integer countByRole(String role) {
        return userMapper.countByRole(role);
    }

    @Override
    public Integer countByCreateTimeRange(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        return userMapper.countByCreateTimeRange(startTime, endTime);
    }

    @Override
    public List<UserDTO> findRecentUsers(Integer limit) {
        List<User> userList = userMapper.findRecentUsers(limit);
        return userList.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findActiveUsers(Integer limit) {
        List<User> userList = userMapper.findActiveUsers(limit);
        return userList.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findAdminUsers(Integer limit) {
        List<User> userList = userMapper.findAdminUsers(limit);
        return userList.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findRegularUsers(Integer limit) {
        List<User> userList = userMapper.findRegularUsers(limit);
        return userList.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> findDisabledUsers(Integer limit) {
        List<User> userList = userMapper.findDisabledUsers(limit);
        return userList.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> findUsernames() {
        return userMapper.findUsernames();
    }

    @Override
    public List<String> findEmails() {
        return userMapper.findEmails();
    }

    @Override
    public List<String> findPhones() {
        return userMapper.findPhones();
    }

    @Override
    public List<String> findNicknames() {
        return userMapper.findNicknames();
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> findRoleDistribution() {
        return userMapper.findRoleDistribution();
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> findStatusDistribution() {
        return userMapper.findStatusDistribution();
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> findRegistrationTrendByDay(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        return userMapper.findRegistrationTrendByDay(startTime, endTime);
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> findRegistrationTrendByMonth(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        return userMapper.findRegistrationTrendByMonth(startTime, endTime);
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> findRegistrationTrendByYear(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        return userMapper.findRegistrationTrendByYear(startTime, endTime);
    }

    @Override
    public java.util.Map<String, Object> findUserActivityStats() {
        return userMapper.findUserActivityStats();
    }

    @Override
    public UserDTO getUser(Long id) {
        // 查询用户
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 转换为DTO
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(userDTO.getUsername());
        if (existingUser != null) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 检查邮箱是否已存在
        existingUser = userMapper.findByEmail(userDTO.getEmail());
        if (existingUser != null) {
            throw new BusinessException(400, "邮箱已存在");
        }

        // 创建用户
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);
        user.setPassword(PasswordUtil.encodePassword(userDTO.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // 保存用户
        userMapper.insert(user);

        // 转换为DTO
        UserDTO createdUserDTO = new UserDTO();
        BeanUtils.copyProperties(user, createdUserDTO);
        return createdUserDTO;
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        // 查询用户
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 检查用户名是否已存在
        User existingUser = userMapper.findByUsername(userDTO.getUsername());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 检查邮箱是否已存在
        existingUser = userMapper.findByEmail(userDTO.getEmail());
        if (existingUser != null && !existingUser.getId().equals(id)) {
            throw new BusinessException(400, "邮箱已存在");
        }

        // 更新用户
        BeanUtils.copyProperties(userDTO, user);
        user.setUpdatedAt(LocalDateTime.now());

        // 保存用户
        userMapper.update(user);

        // 转换为DTO
        UserDTO updatedUserDTO = new UserDTO();
        BeanUtils.copyProperties(user, updatedUserDTO);
        return updatedUserDTO;
    }

    @Override
    public void deleteUser(Long id) {
        // 查询用户
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // 删除用户
        userMapper.deleteById(id);
    }
}