package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {

    /**
     * 插入用户
     *
     * @param user 用户
     * @return 影响行数
     */
    @Insert({
        "<script>",
        "INSERT INTO users (username, password, email, phone, nickname, avatar, status, role, created_at, updated_at)",
        "VALUES (#{username}, #{password}, #{email}, #{phone}, #{nickname}, #{avatar}, #{status}, #{role}, #{createdAt}, #{updatedAt})",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户
     */
    @Select("SELECT * FROM users WHERE id = #{id}")
    User selectById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);

    /**
     * 根据邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);

    /**
     * 查询用户列表
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM users ORDER BY id LIMIT #{offset}, #{limit}")
    List<User> selectAll(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 条件查询用户列表
     *
     * @param offset   偏移量
     * @param limit    限制数量
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
     * @return 用户列表
     */
    List<User> selectAllWithConditions(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("username") String username,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("nickname") String nickname,
            @Param("status") Integer status,
            @Param("role") String role,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder
    );

    /**
     * 查询用户总数
     *
     * @return 总数
     */
    @Select("SELECT COUNT(*) FROM users")
    int countAll();

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
    int countAllWithConditions(
            @Param("username") String username,
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("nickname") String nickname,
            @Param("status") Integer status,
            @Param("role") String role,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime
    );

    /**
     * 根据用户名查询用户数量
     *
     * @param username 用户名
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(@Param("username") String username);

    /**
     * 根据邮箱查询用户数量
     *
     * @param email 邮箱
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(@Param("email") String email);

    /**
     * 根据手机号查询用户数量
     *
     * @param phone 手机号
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE phone = #{phone}")
    int countByPhone(@Param("phone") String phone);

    /**
     * 根据用户名和ID查询用户数量（排除当前用户）
     *
     * @param username 用户名
     * @param id       用户ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE username = #{username} AND id != #{id}")
    int countByUsernameExcludingId(@Param("username") String username, @Param("id") Long id);

    /**
     * 根据邮箱和ID查询用户数量（排除当前用户）
     *
     * @param email 邮箱
     * @param id    用户ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE email = #{email} AND id != #{id}")
    int countByEmailExcludingId(@Param("email") String email, @Param("id") Long id);

    /**
     * 根据手机号和ID查询用户数量（排除当前用户）
     *
     * @param phone 手机号
     * @param id    用户ID
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE phone = #{phone} AND id != #{id}")
    int countByPhoneExcludingId(@Param("phone") String phone, @Param("id") Long id);

    /**
     * 根据状态查询用户数量
     *
     * @param status 状态
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE status = #{status}")
    int countByStatus(@Param("status") Integer status);

    /**
     * 根据角色查询用户数量
     *
     * @param role 角色
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE role = #{role}")
    int countByRole(@Param("role") String role);

    /**
     * 根据创建时间范围查询用户数量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 数量
     */
    @Select("SELECT COUNT(*) FROM users WHERE created_at BETWEEN #{startTime} AND #{endTime}")
    int countByCreateTimeRange(@Param("startTime") java.time.LocalDateTime startTime,
                              @Param("endTime") java.time.LocalDateTime endTime);

    /**
     * 查询最近注册的用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM users ORDER BY created_at DESC LIMIT #{limit}")
    List<User> findRecentUsers(@Param("limit") int limit);

    /**
     * 查询活跃用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE status = 1 ORDER BY last_login_at DESC LIMIT #{limit}")
    List<User> findActiveUsers(@Param("limit") int limit);

    /**
     * 查询管理员用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE role = 'ADMIN' ORDER BY created_at DESC LIMIT #{limit}")
    List<User> findAdminUsers(@Param("limit") int limit);

    /**
     * 查询普通用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE role = 'USER' ORDER BY created_at DESC LIMIT #{limit}")
    List<User> findRegularUsers(@Param("limit") int limit);

    /**
     * 查询禁用用户
     *
     * @param limit 限制数量
     * @return 用户列表
     */
    @Select("SELECT * FROM users WHERE status = 0 ORDER BY updated_at DESC LIMIT #{limit}")
    List<User> findDisabledUsers(@Param("limit") int limit);

    /**
     * 查询用户名列表
     *
     * @return 用户名列表
     */
    @Select("SELECT username FROM users WHERE status = 1 ORDER BY username")
    List<String> findUsernames();

    /**
     * 查询邮箱列表
     *
     * @return 邮箱列表
     */
    @Select("SELECT email FROM users WHERE status = 1 AND email IS NOT NULL AND email != '' ORDER BY email")
    List<String> findEmails();

    /**
     * 查询手机号列表
     *
     * @return 手机号列表
     */
    @Select("SELECT phone FROM users WHERE status = 1 AND phone IS NOT NULL AND phone != '' ORDER BY phone")
    List<String> findPhones();

    /**
     * 查询昵称列表
     *
     * @return 昵称列表
     */
    @Select("SELECT nickname FROM users WHERE status = 1 AND nickname IS NOT NULL AND nickname != '' ORDER BY nickname")
    List<String> findNicknames();

    /**
     * 查询用户角色分布
     *
     * @return 角色分布
     */
    @Select("SELECT role, COUNT(*) as count FROM users GROUP BY role")
    java.util.List<java.util.Map<String, Object>> findRoleDistribution();

    /**
     * 查询用户状态分布
     *
     * @return 状态分布
     */
    @Select("SELECT status, COUNT(*) as count FROM users GROUP BY status")
    java.util.List<java.util.Map<String, Object>> findStatusDistribution();

    /**
     * 查询用户注册趋势（按天）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    @Select("SELECT DATE(created_at) as date, COUNT(*) as count FROM users WHERE created_at BETWEEN #{startTime} AND #{endTime} GROUP BY DATE(created_at) ORDER BY date")
    java.util.List<java.util.Map<String, Object>> findRegistrationTrendByDay(
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime
    );

    /**
     * 查询用户注册趋势（按月）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    @Select("SELECT DATE_FORMAT(created_at, '%Y-%m') as month, COUNT(*) as count FROM users WHERE created_at BETWEEN #{startTime} AND #{endTime} GROUP BY DATE_FORMAT(created_at, '%Y-%m') ORDER BY month")
    java.util.List<java.util.Map<String, Object>> findRegistrationTrendByMonth(
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime
    );

    /**
     * 查询用户注册趋势（按年）
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 注册趋势
     */
    @Select("SELECT DATE_FORMAT(created_at, '%Y') as year, COUNT(*) as count FROM users WHERE created_at BETWEEN #{startTime} AND #{endTime} GROUP BY DATE_FORMAT(created_at, '%Y') ORDER BY year")
    java.util.List<java.util.Map<String, Object>> findRegistrationTrendByYear(
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime
    );

    /**
     * 查询用户活跃度统计
     *
     * @return 活跃度统计
     */
    @Select("SELECT COUNT(*) as total_users, COUNT(CASE WHEN status = 1 THEN 1 END) as active_users, COUNT(CASE WHEN status = 0 THEN 1 END) as inactive_users, COUNT(CASE WHEN role = 'ADMIN' THEN 1 END) as admin_users, COUNT(CASE WHEN role = 'USER' THEN 1 END) as regular_users FROM users")
    java.util.Map<String, Object> findUserActivityStats();

    /**
     * 更新用户
     *
     * @param user 用户
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE users",
        "<set>",
        "<if test='username != null'>username = #{username},</if>",
        "<if test='password != null'>password = #{password},</if>",
        "<if test='email != null'>email = #{email},</if>",
        "<if test='phone != null'>phone = #{phone},</if>",
        "<if test='nickname != null'>nickname = #{nickname},</if>",
        "<if test='avatar != null'>avatar = #{avatar},</if>",
        "<if test='status != null'>status = #{status},</if>",
        "<if test='role != null'>role = #{role},</if>",
        "updated_at = #{updatedAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(User user);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     * @return 影响行数
     */
    @Delete("DELETE FROM users WHERE id = #{id}")
    int deleteById(Long id);
}