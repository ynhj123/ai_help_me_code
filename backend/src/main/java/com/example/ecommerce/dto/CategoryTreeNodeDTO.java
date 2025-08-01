package com.example.ecommerce.dto;

import lombok.Data;

import java.util.List;

/**
 * 分类树节点DTO类
 */
@Data
public class CategoryTreeNodeDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 父级分类ID
     */
    private Long parentId;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 子分类列表
     */
    private List<CategoryTreeNodeDTO> children;

    /**
     * 是否有子分类
     */
    private Boolean hasChildren;

    /**
     * 层级深度
     */
    private Integer depth;

    /**
     * 路径（用于面包屑导航）
     */
    private String path;
}