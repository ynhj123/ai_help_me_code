package com.example.ecommerce.controller;

import com.example.ecommerce.dto.UserDTO;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取当前用户信息
     *
     * @return 用户信息
     */
    @GetMapping("/profile")
    public ResultVO<UserDTO> getProfile() {
        UserDTO userDTO = userService.getProfile();
        return ResultVO.success(userDTO);
    }

    /**
     * 获取用户列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 用户列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<UserDTO>> getUserList(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        List<UserDTO> userList = userService.getUserList(page, size);
        return ResultVO.success(userList);
    }

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
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<UserDTO>> searchUsers(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        
        // 转换时间字符串
        java.time.LocalDateTime start = null;
        java.time.LocalDateTime end = null;
        
        if (startTime != null && !startTime.isEmpty()) {
            start = java.time.LocalDateTime.parse(startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            end = java.time.LocalDateTime.parse(endTime);
        }
        
        List<UserDTO> userList = userService.getUserList(username, email, phone, nickname,
                status, role, start, end, sortBy, sortOrder, page, size);
        return ResultVO.success(userList);
    }

    /**
     * 获取用户总数
     *
     * @return 总数
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> getUserCount() {
        Integer count = userService.getUserCount();
        return ResultVO.success(count);
    }

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
    @GetMapping("/count/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> searchUserCount(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {
        
        // 转换时间字符串
        java.time.LocalDateTime start = null;
        java.time.LocalDateTime end = null;
        
        if (startTime != null && !startTime.isEmpty()) {
            start = java.time.LocalDateTime.parse(startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            end = java.time.LocalDateTime.parse(endTime);
        }
        
        Integer count = userService.getUserCount(username, email, phone, nickname,
                status, role, start, end);
        return ResultVO.success(count);
    }

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    @GetMapping("/exists/username")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> existsByUsername(@RequestParam String username) {
        Boolean exists = userService.existsByUsername(username);
        return ResultVO.success(exists);
    }

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    @GetMapping("/exists/email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> existsByEmail(@RequestParam String email) {
        Boolean exists = userService.existsByEmail(email);
        return ResultVO.success(exists);
    }

    /**
     * 检查手机号是否已存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    @GetMapping("/exists/phone")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> existsByPhone(@RequestParam String phone) {
        Boolean exists = userService.existsByPhone(phone);
        return ResultVO.success(exists);
    }

    /**
     * 检查用户名是否已存在（排除当前用户）
     *
     * @param username 用户名
     * @param id       用户ID
     * @return 是否存在
     */
    @GetMapping("/exists/username/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> existsByUsernameExcludingId(@PathVariable Long id, @RequestParam String username) {
        Boolean exists = userService.existsByUsernameExcludingId(username, id);
        return ResultVO.success(exists);
    }

    /**
     * 检查邮箱是否已存在（排除当前用户）
     *
     * @param email 邮箱
     * @param id    用户ID
     * @return 是否存在
     */
    @GetMapping("/exists/email/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> existsByEmailExcludingId(@PathVariable Long id, @RequestParam String email) {
        Boolean exists = userService.existsByEmailExcludingId(email, id);
        return ResultVO.success(exists);
    }

    /**
     * 检查手机号是否已存在（排除当前用户）
     *
     * @param phone 手机号
     * @param id    用户ID
     * @return 是否存在
     */
    @GetMapping("/exists/phone/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> existsByPhoneExcludingId(@PathVariable Long id, @RequestParam String phone) {
        Boolean exists = userService.existsByPhoneExcludingId(phone, id);
        return ResultVO.success(exists);
    }

    /**
     * 根据状态查询用户数量
     *
     * @param status 状态
     * @return 数量
     */
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> countByStatus(@PathVariable Integer status) {
        Integer count = userService.countByStatus(status);
        return ResultVO.success(count);
    }

    /**
     * 根据角色查询用户数量
     *
     * @param role 角色
     * @return 数量
     */
    @GetMapping("/count/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> countByRole(@PathVariable String role) {
        Integer count = userService.countByRole(role);
        return ResultVO.success(count);
    }

    /**
     * 根据创建时间范围查询用户数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 数量
     */
    @GetMapping("/count/time-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> countByCreateTimeRange(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        java.time.LocalDateTime start = java.time.LocalDateTime.parse(startTime);
        java.time.LocalDateTime end = java.time.LocalDateTime.parse(endTime);
        
        Integer count = userService.countByCreateTimeRange(start, end);
        return ResultVO.success(count);
    }

    /**
     * 查询最近注册的用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @GetMapping("/recent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<UserDTO>> findRecentUsers(@RequestParam(defaultValue = "10") Integer limit) {
        List<UserDTO> users = userService.findRecentUsers(limit);
        return ResultVO.success(users);
    }

    /**
     * 查询活跃用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<UserDTO>> findActiveUsers(@RequestParam(defaultValue = "10") Integer limit) {
        List<UserDTO> users = userService.findActiveUsers(limit);
        return ResultVO.success(users);
    }

    /**
     * 查询管理员用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<UserDTO>> findAdminUsers(@RequestParam(defaultValue = "10") Integer limit) {
        List<UserDTO> users = userService.findAdminUsers(limit);
        return ResultVO.success(users);
    }

    /**
     * 查询普通用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @GetMapping("/regular")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<UserDTO>> findRegularUsers(@RequestParam(defaultValue = "10") Integer limit) {
        List<UserDTO> users = userService.findRegularUsers(limit);
        return ResultVO.success(users);
    }

    /**
     * 查询禁用用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @GetMapping("/disabled")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<UserDTO>> findDisabledUsers(@RequestParam(defaultValue = "10") Integer limit) {
        List<UserDTO> users = userService.findDisabledUsers(limit);
        return ResultVO.success(users);
    }

    /**
     * 查询用户名列表
     *
     * @return 用户名列表
     */
    @GetMapping("/usernames")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<String>> findUsernames() {
        List<String> usernames = userService.findUsernames();
        return ResultVO.success(usernames);
    }

    /**
     * 查询邮箱列表
     *
     * @return 邮箱列表
     */
    @GetMapping("/emails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<String>> findEmails() {
        List<String> emails = userService.findEmails();
        return ResultVO.success(emails);
    }

    /**
     * 查询手机号列表
     *
     * @return 手机号列表
     */
    @GetMapping("/phones")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<String>> findPhones() {
        List<String> phones = userService.findPhones();
        return ResultVO.success(phones);
    }

    /**
     * 查询昵称列表
     *
     * @return 昵称列表
     */
    @GetMapping("/nicknames")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<String>> findNicknames() {
        List<String> nicknames = userService.findNicknames();
        return ResultVO.success(nicknames);
    }

    /**
     * 查询用户角色分布
     *
     * @return 角色分布
     */
    @GetMapping("/stats/role-distribution")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<java.util.Map<String, Object>>> findRoleDistribution() {
        List<java.util.Map<String, Object>> distribution = userService.findRoleDistribution();
        return ResultVO.success(distribution);
    }

    /**
     * 查询用户状态分布
     *
     * @return 状态分布
     */
    @GetMapping("/stats/status-distribution")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<java.util.Map<String, Object>>> findStatusDistribution() {
        List<java.util.Map<String, Object>> distribution = userService.findStatusDistribution();
        return ResultVO.success(distribution);
    }

    /**
     * 查询用户注册趋势（按天）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    @GetMapping("/stats/registration-trend/day")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<java.util.Map<String, Object>>> findRegistrationTrendByDay(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        java.time.LocalDateTime start = java.time.LocalDateTime.parse(startTime);
        java.time.LocalDateTime end = java.time.LocalDateTime.parse(endTime);
        
        List<java.util.Map<String, Object>> trend = userService.findRegistrationTrendByDay(start, end);
        return ResultVO.success(trend);
    }

    /**
     * 查询用户注册趋势（按月）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    @GetMapping("/stats/registration-trend/month")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<java.util.Map<String, Object>>> findRegistrationTrendByMonth(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        java.time.LocalDateTime start = java.time.LocalDateTime.parse(startTime);
        java.time.LocalDateTime end = java.time.LocalDateTime.parse(endTime);
        
        List<java.util.Map<String, Object>> trend = userService.findRegistrationTrendByMonth(start, end);
        return ResultVO.success(trend);
    }

    /**
     * 查询用户注册趋势（按年）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    @GetMapping("/stats/registration-trend/year")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<java.util.Map<String, Object>>> findRegistrationTrendByYear(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        
        java.time.LocalDateTime start = java.time.LocalDateTime.parse(startTime);
        java.time.LocalDateTime end = java.time.LocalDateTime.parse(endTime);
        
        List<java.util.Map<String, Object>> trend = userService.findRegistrationTrendByYear(start, end);
        return ResultVO.success(trend);
    }

    /**
     * 查询用户活跃度统计
     *
     * @return 活跃度统计
     */
    @GetMapping("/stats/activity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<java.util.Map<String, Object>> findUserActivityStats() {
        java.util.Map<String, Object> stats = userService.findUserActivityStats();
        return ResultVO.success(stats);
    }

    /**
     * 获取用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<UserDTO> getUser(@PathVariable Long id) {
        UserDTO userDTO = userService.getUser(id);
        return ResultVO.success(userDTO);
    }

    /**
     * 创建用户
     *
     * @param userDTO 用户信息
     * @return 创建结果
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResultVO.success("用户创建成功", createdUser);
    }

    /**
     * 更新用户
     *
     * @param id      用户ID
     * @param userDTO 用户信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        return ResultVO.success("用户更新成功", updatedUser);
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResultVO.success("用户删除成功", null);
    }
}