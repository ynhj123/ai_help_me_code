package com.example.ecommerce.dto;

import com.example.ecommerce.enums.ProductStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品DTO类
 */
@Data
public class ProductDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @DecimalMax(value = "999999.99", message = "价格不能超过999999.99")
    private BigDecimal price;

    /**
     * 市场价
     */
    @DecimalMin(value = "0.01", message = "市场价必须大于等于0")
    @DecimalMax(value = "999999.99", message = "市场价不能超过999999.99")
    private BigDecimal marketPrice;

    /**
     * 成本价
     */
    @DecimalMin(value = "0.01", message = "成本价必须大于等于0")
    @DecimalMax(value = "999999.99", message = "成本价不能超过999999.99")
    private BigDecimal costPrice;

    /**
     * SKU
     */
    @Size(max = 100, message = "SKU长度不能超过100个字符")
    private String sku;

    /**
     * 条形码
     */
    @Size(max = 100, message = "条形码长度不能超过100个字符")
    private String barcode;

    /**
     * 商品图片
     */
    @Size(max = 255, message = "商品图片路径长度不能超过255个字符")
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
    @NotNull(message = "状态不能为空")
    private ProductStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}