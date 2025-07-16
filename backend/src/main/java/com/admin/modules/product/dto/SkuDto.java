package com.admin.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkuDto {
    private Long id;
    private String skuCode;
    private String name;
    private BigDecimal price;
    private BigDecimal marketPrice;
    private Integer stock;
    private Integer warningStock;
    private String barcode;
    private BigDecimal weight;
    private String specifications;
    private String status;
}