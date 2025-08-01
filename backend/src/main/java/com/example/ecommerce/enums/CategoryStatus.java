package com.example.ecommerce.enums;

/**
 * 分类状态枚举
 */
public enum CategoryStatus {
    /**
     * 显示
     */
    ACTIVE(1, "显示"),
    
    /**
     * 隐藏
     */
    INACTIVE(0, "隐藏");

    private final int code;
    private final String description;

    CategoryStatus(int code, String description) {
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
     * 根据代码获取分类状态
     *
     * @param code 状态代码
     * @return 分类状态枚举
     */
    public static CategoryStatus fromCode(int code) {
        for (CategoryStatus status : CategoryStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的分类状态代码: " + code);
    }
}