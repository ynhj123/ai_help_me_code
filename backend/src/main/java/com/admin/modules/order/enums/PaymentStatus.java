package com.admin.modules.order.enums;

public enum PaymentStatus {
    UNPAID,      // 未支付
    PAID,        // 已支付
    REFUNDING,   // 退款中
    REFUNDED,    // 已退款
    FAILED       // 支付失败
}