package com.admin.modules.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryCreateRequest {
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String name;

    @NotBlank(message = "分类编码不能为空")
    @Size(max = 50, message = "分类编码长度不能超过50个字符")
    private String code;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    private String description;

    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    private String imageUrl;

    @NotNull(message = "排序值不能为空")
    private Integer sortOrder = 0;

    private Long parentId;
}