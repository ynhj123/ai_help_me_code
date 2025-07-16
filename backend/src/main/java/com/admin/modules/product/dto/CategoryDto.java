package com.admin.modules.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String imageUrl;
    private Integer sortOrder;
    private Long parentId;
    private String parentName;
    private String status;
    private List<CategoryDto> children;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}