package com.example.ecommerce.dto;

import com.example.ecommerce.enums.CartStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车DTO类
 */
@Data
public class CartDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long productId;

    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 255, message = "商品名称长度不能超过255个字符")
    private String productName;

    /**
     * 商品图片
     */
    private String productImage;

    /**
     * 商品SKU
     */
    @NotBlank(message = "商品SKU不能为空")
    @Size(max = 100, message = "商品SKU长度不能超过100个字符")
    private String productSku;

    /**
     * 商品单价
     */
    @NotNull(message = "商品单价不能为空")
    @DecimalMin(value = "0.01", message = "商品单价必须大于0")
    private BigDecimal productPrice;

    /**
     * 购买数量
     */
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于0")
    private Integer quantity;

    /**
     * 商品总金额
     */
    @NotNull(message = "商品总金额不能为空")
    @DecimalMin(value = "0.01", message = "商品总金额必须大于0")
    private BigDecimal subtotal;

    /**
     * 商品分类ID
     */
    @NotNull(message = "商品分类ID不能为空")
    private Long categoryId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private CartStatus status;

    /**
     * 选中状态
     */
    private Boolean selected = true;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}