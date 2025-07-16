package com.admin.modules.product.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ProductImageCreateRequest {
    @NotBlank(message = "图片URL不能为空")
    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    private String url;

    @Size(max = 200, message = "图片描述长度不能超过200个字符")
    private String alt;

    @NotNull(message = "排序值不能为空")
    private Integer sortOrder = 0;

    private Boolean isMain = false;
}