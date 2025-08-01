package com.example.ecommerce.enums;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    /**
     * 待付款
     */
    PENDING_PAYMENT(1, "待付款"),
    
    /**
     * 已付款
     */
    PAID(2, "已付款"),
    
    /**
     * 已发货
     */
    SHIPPED(3, "已发货"),
    
    /**
     * 已完成
     */
    COMPLETED(4, "已完成"),
    
    /**
     * 已取消
     */
    CANCELLED(5, "已取消"),
    
    /**
     * 已退款
     */
    REFUNDED(6, "已退款");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
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
     * 根据代码获取订单状态
     *
     * @param code 状态代码
     * @return 订单状态枚举
     */
    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的订单状态代码: " + code);
    }
}