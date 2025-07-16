package com.admin.modules.logistics.entity;

import com.admin.common.entity.BaseEntity;
import com.admin.modules.logistics.enums.DeliveryMethod;
import com.admin.modules.logistics.enums.ShippingStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipping_orders")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShippingOrder extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String trackingNumber;
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    private Long userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryMethod deliveryMethod;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrier_id")
    private Carrier carrier;
    
    @Column(length = 100)
    private String localDeliveryPerson;
    
    @Column(length = 20)
    private String localDeliveryPhone;
    
    @Column(length = 500)
    private String pickupAddress;
    
    @Column(length = 500)
    private String pickupInstructions;
    
    @Column(length = 100)
    private String receiverName;
    
    @Column(length = 20)
    private String receiverPhone;
    
    @Column(length = 500)
    private String receiverAddress;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal shippingFee;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal weight;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal volume;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingStatus status = ShippingStatus.PENDING;
    
    @Column
    private LocalDateTime shippedAt;
    
    @Column
    private LocalDateTime estimatedDeliveryAt;
    
    @Column
    private LocalDateTime deliveredAt;
    
    @Column(length = 1000)
    private String trackingInfo;
    
    @Column(length = 1000)
    private String deliveryNotes;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;
}