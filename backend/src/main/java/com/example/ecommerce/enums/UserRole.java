package com.example.ecommerce.enums;

/**
 * 用户角色枚举
 */
public enum UserRole {
    /**
     * 管理员
     */
    ADMIN("ADMIN", "管理员"),

    /**
     * 分销商
     */
    DISTRIBUTOR("DISTRIBUTOR", "分销商"),

    /**
     * 运输人员
     */
    DELIVERY_PERSONNEL("DELIVERY_PERSONNEL", "运输人员"),

    /**
     * 普通用户
     */
    USER("USER", "普通用户");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取用户角色
     *
     * @param code 角色代码
     * @return 用户角色枚举
     */
    public static UserRole fromCode(String code) {
        for (UserRole role : UserRole.values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("未知的用户角色代码: " + code);
    }
}