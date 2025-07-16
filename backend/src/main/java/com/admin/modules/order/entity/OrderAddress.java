package com.admin.modules.order.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "order_addresses")
@Data
public class OrderAddress {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @Column(nullable = false, length = 50)
    private String receiverName;
    
    @Column(nullable = false, length = 20)
    private String receiverPhone;
    
    @Column(nullable = false, length = 100)
    private String province;
    
    @Column(nullable = false, length = 100)
    private String city;
    
    @Column(nullable = false, length = 100)
    private String district;
    
    @Column(nullable = false, length = 200)
    private String address;
    
    @Column(length = 20)
    private String postalCode;
    
    @Column(length = 500)
    private String remark;
}