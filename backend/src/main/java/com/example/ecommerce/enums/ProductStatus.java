package com.example.ecommerce.enums;

/**
 * 商品状态枚举
 */
public enum ProductStatus {
    /**
     * 上架
     */
    ACTIVE(1, "上架"),
    
    /**
     * 下架
     */
    INACTIVE(0, "下架");

    private final int code;
    private final String description;

    ProductStatus(int code, String description) {
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
     * 根据代码获取商品状态
     *
     * @param code 状态代码
     * @return 商品状态枚举
     */
    public static ProductStatus fromCode(int code) {
        for (ProductStatus status : ProductStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的商品状态代码: " + code);
    }
}