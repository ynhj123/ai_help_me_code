package com.example.ecommerce.dto;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 商品查询请求DTO类
 */
@Data
public class ProductQueryRequest {

    /**
     * 页码
     */
    @NotNull(message = "页码不能为空")
    @Min(value = 1, message = "页码必须大于0")
    private Integer page = 1;

    /**
     * 每页大小
     */
    @NotNull(message = "每页大小不能为空")
    @Min(value = 1, message = "每页大小必须大于0")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer size = 10;

    /**
     * 商品名称（模糊查询）
     */
    private String name;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * SKU（精确查询）
     */
    private String sku;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 排序字段
     */
    private String sortBy = "id";

    /**
     * 排序方向：asc/desc
     */
    private String sortOrder = "desc";
}