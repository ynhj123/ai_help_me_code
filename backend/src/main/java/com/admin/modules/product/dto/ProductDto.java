package com.admin.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private Long id;
    private String name;
    private String code;
    private Long categoryId;
    private String categoryName;
    private String categoryPath;
    private String brand;
    private String description;
    private String richContent;
    private String status;
    private List<SkuDto> skus;
    private List<ProductImageDto> images;
    private List<ProductAttributeDto> attributes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}