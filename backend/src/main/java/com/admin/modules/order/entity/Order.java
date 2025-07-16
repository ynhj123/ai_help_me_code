package com.admin.modules.order.entity;

import com.admin.common.entity.BaseEntity;
import com.admin.modules.order.enums.OrderStatus;
import com.admin.modules.order.enums.PaymentStatus;
import com.admin.modules.order.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Order extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 32)
    private String orderNumber;
    
    @Column(nullable = false)
    private Long userId;
    
    @Column(nullable = false, length = 50)
    private String userName;
    
    @Column(length = 20)
    private String userPhone;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal payableAmount;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID;
    
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    
    @Column(length = 500)
    private String remark;
    
    @Column(length = 500)
    private String cancelReason;
    
    @Column
    private LocalDateTime paidAt;
    
    @Column
    private LocalDateTime shippedAt;
    
    @Column
    private LocalDateTime deliveredAt;
    
    @Column
    private LocalDateTime completedAt;
    
    @Column
    private LocalDateTime cancelledAt;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderAddress address;
    
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OrderPayment payment;
}