package com.admin.modules.logistics.entity;

import com.admin.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "carriers")
@Data
@EqualsAndHashCode(callSuper = true)
public class Carrier extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, unique = true, length = 50)
    private String code;
    
    @Column(length = 500)
    private String description;
    
    @Column(length = 200)
    private String contactPerson;
    
    @Column(length = 20)
    private String contactPhone;
    
    @Column(length = 200)
    private String contactEmail;
    
    @Column(length = 500)
    private String address;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false)
    private Boolean isDeleted = false;
    
    @Column(precision = 3, scale = 2)
    private Double basePrice;
    
    @Column(precision = 3, scale = 2)
    private Double pricePerKg;
    
    @Column(precision = 3, scale = 2)
    private Double pricePerKm;
}