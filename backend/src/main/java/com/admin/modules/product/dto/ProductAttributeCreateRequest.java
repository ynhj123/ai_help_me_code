package com.admin.modules.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductAttributeCreateRequest {
    @NotBlank(message = "属性名称不能为空")
    @Size(max = 100, message = "属性名称长度不能超过100个字符")
    private String name;

    @NotBlank(message = "属性值不能为空")
    @Size(max = 500, message = "属性值长度不能超过500个字符")
    private String value;

    @NotNull(message = "排序值不能为空")
    private Integer sortOrder = 0;

    private Boolean isSaleAttribute = false;

    @Size(max = 50, message = "分组名称长度不能超过50个字符")
    private String groupName;
}