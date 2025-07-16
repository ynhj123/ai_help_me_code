package com.admin.modules.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuCreateRequest {
    @NotBlank(message = "SKU编码不能为空")
    @Size(max = 50, message = "SKU编码长度不能超过50个字符")
    private String skuCode;

    @NotBlank(message = "SKU名称不能为空")
    @Size(max = 200, message = "SKU名称长度不能超过200个字符")
    private String name;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格必须大于0")
    @Digits(integer = 10, fraction = 2, message = "价格格式不正确")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "市场价不能为负数")
    @Digits(integer = 10, fraction = 2, message = "市场价格式不正确")
    private BigDecimal marketPrice;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer stock;

    @Min(value = 0, message = "预警库存不能为负数")
    private Integer warningStock;

    @Size(max = 50, message = "条码长度不能超过50个字符")
    private String barcode;

    @DecimalMin(value = "0.00", message = "重量不能为负数")
    @Digits(integer = 10, fraction = 3, message = "重量格式不正确")
    private BigDecimal weight;

    private String specifications;
}