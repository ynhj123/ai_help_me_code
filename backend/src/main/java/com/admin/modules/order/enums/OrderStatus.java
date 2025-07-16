package com.admin.modules.order.enums;

public enum OrderStatus {
    PENDING,        // 待付款
    PAID,          // 已付款
    PROCESSING,    // 处理中
    SHIPPED,       // 已发货
    DELIVERED,     // 已送达
    COMPLETED,     // 已完成
    CANCELLED,     // 已取消
    REFUNDED       // 已退款
}