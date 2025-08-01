package com.example.ecommerce.dto;

import com.example.ecommerce.enums.CategoryStatus;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * 分类DTO类
 */
@Data
public class CategoryDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 100, message = "分类名称长度不能超过100个字符")
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父级分类ID
     */
    @NotNull(message = "父级分类ID不能为空")
    private Long parentId;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @Min(value = 0, message = "排序必须大于等于0")
    private Integer sortOrder;

    /**
     * 状态
     */
    @NotNull(message = "状态不能为空")
    private CategoryStatus status;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}