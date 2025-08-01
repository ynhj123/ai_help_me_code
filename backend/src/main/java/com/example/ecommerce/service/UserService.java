package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserDTO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return JWT token
     */
    String login(LoginRequest loginRequest);

    /**
     * 用户注册
     *
     * @param registerRequest 注册请求
     */
    void register(RegisterRequest registerRequest);

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    UserDTO getProfile();

    /**
     * 获取用户列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 用户列表
     */
    List<UserDTO> getUserList(Integer page, Integer size);

    /**
     * 条件查询用户列表
     *
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     * @param nickname 昵称
     * @param status   状态
     * @param role     角色
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param sortBy    排序字段
     * @param sortOrder 排序方式
     * @param page      页码
     * @param size      每页大小
     * @return 用户列表
     */
    List<UserDTO> getUserList(String username, String email, String phone, String nickname,
                             Integer status, String role, java.time.LocalDateTime startTime,
                             java.time.LocalDateTime endTime, String sortBy, String sortOrder,
                             Integer page, Integer size);

    /**
     * 获取用户总数
     *
     * @return 总数
     */
    Integer getUserCount();

    /**
     * 条件查询用户总数
     *
     * @param username 用户名
     * @param email    邮箱
     * @param phone    手机号
     * @param nickname 昵称
     * @param status   状态
     * @param role     角色
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 总数
     */
    Integer getUserCount(String username, String email, String phone, String nickname,
                        Integer status, String role, java.time.LocalDateTime startTime,
                        java.time.LocalDateTime endTime);

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    Boolean existsByUsername(String username);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    Boolean existsByEmail(String email);

    /**
     * 检查手机号是否已存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    Boolean existsByPhone(String phone);

    /**
     * 检查用户名是否已存在（排除当前用户）
     *
     * @param username 用户名
     * @param id       用户ID
     * @return 是否存在
     */
    Boolean existsByUsernameExcludingId(String username, Long id);

    /**
     * 检查邮箱是否已存在（排除当前用户）
     *
     * @param email 邮箱
     * @param id    用户ID
     * @return 是否存在
     */
    Boolean existsByEmailExcludingId(String email, Long id);

    /**
     * 检查手机号是否已存在（排除当前用户）
     *
     * @param phone 手机号
     * @param id    用户ID
     * @return 是否存在
     */
    Boolean existsByPhoneExcludingId(String phone, Long id);

    /**
     * 根据状态查询用户数量
     *
     * @param status 状态
     * @return 数量
     */
    Integer countByStatus(Integer status);

    /**
     * 根据角色查询用户数量
     *
     * @param role 角色
     * @return 数量
     */
    Integer countByRole(String role);

    /**
     * 根据创建时间范围查询用户数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 数量
     */
    Integer countByCreateTimeRange(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * 查询最近注册的用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserDTO> findRecentUsers(Integer limit);

    /**
     * 查询活跃用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserDTO> findActiveUsers(Integer limit);

    /**
     * 查询管理员用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserDTO> findAdminUsers(Integer limit);

    /**
     * 查询普通用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserDTO> findRegularUsers(Integer limit);

    /**
     * 查询禁用用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    List<UserDTO> findDisabledUsers(Integer limit);

    /**
     * 查询用户名列表
     *
     * @return 用户名列表
     */
    List<String> findUsernames();

    /**
     * 查询邮箱列表
     *
     * @return 邮箱列表
     */
    List<String> findEmails();

    /**
     * 查询手机号列表
     *
     * @return 手机号列表
     */
    List<String> findPhones();

    /**
     * 查询昵称列表
     *
     * @return 昵称列表
     */
    List<String> findNicknames();

    /**
     * 查询用户角色分布
     *
     * @return 角色分布
     */
    java.util.List<java.util.Map<String, Object>> findRoleDistribution();

    /**
     * 查询用户状态分布
     *
     * @return 状态分布
     */
    java.util.List<java.util.Map<String, Object>> findStatusDistribution();

    /**
     * 查询用户注册趋势（按天）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    java.util.List<java.util.Map<String, Object>> findRegistrationTrendByDay(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * 查询用户注册趋势（按月）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    java.util.List<java.util.Map<String, Object>> findRegistrationTrendByMonth(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * 查询用户注册趋势（按年）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    java.util.List<java.util.Map<String, Object>> findRegistrationTrendByYear(java.time.LocalDateTime startTime, java.time.LocalDateTime endTime);

    /**
     * 查询用户活跃度统计
     *
     * @return 活跃度统计
     */
    java.util.Map<String, Object> findUserActivityStats();

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserDTO getUser(Long id);

    /**
     * 创建用户
     *
     * @param userDTO 用户信息
     * @return 创建的用户
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * 更新用户
     *
     * @param id      用户ID
     * @param userDTO 用户信息
     * @return 更新后的用户
     */
    UserDTO updateUser(Long id, UserDTO userDTO);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);
}