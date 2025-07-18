package com.admin.modules.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreateRequest {
    @NotBlank(message = "商品名称不能为空")
    @Size(max = 200, message = "商品名称长度不能超过200个字符")
    private String name;

    @NotBlank(message = "商品编码不能为空")
    @Size(max = 50, message = "商品编码长度不能超过50个字符")
    private String code;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId;

    @Size(max = 100, message = "品牌长度不能超过100个字符")
    private String brand;

    @Size(max = 1000, message = "商品描述长度不能超过1000个字符")
    private String description;

    private String richContent;

    @NotEmpty(message = "SKU列表不能为空")
    @Valid
    private List<SkuCreateRequest> skus;

    @Valid
    private List<ProductImageCreateRequest> images;

    @Valid
    private List<ProductAttributeCreateRequest> attributes;
}