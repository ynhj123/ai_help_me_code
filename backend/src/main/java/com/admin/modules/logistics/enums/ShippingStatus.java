package com.admin.modules.logistics.enums;

public enum ShippingStatus {
    PENDING,          // 待发货
    SHIPPED,          // 已发货
    IN_TRANSIT,       // 运输中
    OUT_FOR_DELIVERY, // 派送中
    DELIVERED,        // 已送达
    FAILED_DELIVERY,  // 派送失败
    RETURNED          // 已退回
}