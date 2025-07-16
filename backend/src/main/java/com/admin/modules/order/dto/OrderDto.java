package com.admin.modules.order.dto;

import com.admin.modules.order.enums.OrderStatus;
import com.admin.modules.order.enums.PaymentStatus;
import com.admin.modules.order.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private String orderNumber;
    private Long userId;
    private String userName;
    private String userPhone;
    private BigDecimal totalAmount;
    private BigDecimal payableAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private BigDecimal taxAmount;
    private OrderStatus status;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String remark;
    private String cancelReason;
    private LocalDateTime paidAt;
    private LocalDateTime shippedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private List<OrderItemDto> items;
    private OrderAddressDto address;
    private OrderPaymentDto payment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}