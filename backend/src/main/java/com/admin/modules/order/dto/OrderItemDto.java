package com.admin.modules.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    private Long skuId;
    private String productName;
    private String productImage;
    private String skuName;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private Integer quantity;
    private BigDecimal totalAmount;
    private BigDecimal discountAmount;
    private String specifications;
}