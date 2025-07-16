package com.admin.modules.logistics.entity;

import com.admin.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Entity
@Table(name = "shipping_tracks")
@Data
@EqualsAndHashCode(callSuper = true)
public class ShippingTrack extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_order_id", nullable = false)
    private ShippingOrder shippingOrder;
    
    @Column(nullable = false, length = 50)
    private String status;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 200)
    private String location;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(length = 1000)
    private String notes;
}