package com.admin.modules.product.entity;

import com.admin.common.entity.BaseEntity;
import com.admin.modules.product.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "skus")
@Data
@EqualsAndHashCode(callSuper = true)
public class Sku extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String skuCode;
    
    @Column(nullable = false)
    private String name;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal originalPrice;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal marketPrice;
    
    private Integer stock;
    
    private Integer warningStock;
    
    private String image;
    
    private String specifications;
    
    @Column(precision = 10, scale = 3)
    private BigDecimal weight;
    
    @Column(length = 50)
    private String barcode;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    

}