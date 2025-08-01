package com.example.ecommerce.enums;

/**
 * 购物车状态枚举
 */
public enum CartStatus {
    /**
     * 正常
     */
    ACTIVE(1, "正常"),
    
    /**
     * 已删除
     */
    DELETED(0, "已删除");

    private final int code;
    private final String description;

    CartStatus(int code, String description) {
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
     * 根据代码获取购物车状态
     *
     * @param code 状态代码
     * @return 购物车状态枚举
     */
    public static CartStatus fromCode(int code) {
        for (CartStatus status : CartStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的购物车状态代码: " + code);
    }
}