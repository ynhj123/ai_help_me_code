package com.admin.modules.product.entity;

import com.admin.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "product_images")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String url;
    
    @Column(nullable = false)
    private Integer sortOrder = 0;
    
    @Column(length = 500)
    private String alt;
    
    @Column(name = "is_main", nullable = false)
    private Boolean isMain = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}