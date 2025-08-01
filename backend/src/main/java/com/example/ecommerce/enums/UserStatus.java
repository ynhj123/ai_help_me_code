package com.example.ecommerce.enums;

/**
 * 用户状态枚举
 */
public enum UserStatus {
    /**
     * 正常
     */
    ACTIVE(1, "正常"),

    /**
     * 禁用
     */
    DISABLED(0, "禁用");

    private final int code;
    private final String description;

    UserStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取用户状态
     *
     * @param code 状态代码
     * @return 用户状态枚举
     */
    public static UserStatus fromCode(int code) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的用户状态代码: " + code);
    }
}