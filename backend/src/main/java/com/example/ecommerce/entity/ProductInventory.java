package com.example.ecommerce.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 商品库存实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductInventory {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 库存数量
     */
    private Integer quantity;

    /**
     * 预留库存数量
     */
    private Integer reservedQuantity;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}