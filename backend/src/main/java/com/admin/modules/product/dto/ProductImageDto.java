package com.admin.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImageDto {
    private Long id;
    private String url;
    private String alt;
    private Integer sortOrder;
    private Boolean isMain;
}