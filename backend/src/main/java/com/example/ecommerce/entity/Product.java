package com.example.ecommerce.entity;

import com.example.ecommerce.enums.ProductStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 市场价
     */
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    private BigDecimal costPrice;

    /**
     * SKU
     */
    private String sku;

    /**
     * 条形码
     */
    private String barcode;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 商品图集
     */
    private String gallery;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 状态
     */
    private ProductStatus status;
}