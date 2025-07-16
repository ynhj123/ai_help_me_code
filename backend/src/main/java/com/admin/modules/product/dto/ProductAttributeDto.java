package com.admin.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductAttributeDto {
    private Long id;
    private String name;
    private String value;
    private Integer sortOrder;
    private Boolean isSaleAttribute;
    private String groupName;
}