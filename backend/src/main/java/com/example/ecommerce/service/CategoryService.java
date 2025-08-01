package com.example.ecommerce.service;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.dto.CategoryTreeNodeDTO;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService {

    /**
     * 创建分类
     *
     * @param categoryDTO 分类信息
     * @return 创建的分类
     */
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    /**
     * 更新分类
     *
     * @param id          分类ID
     * @param categoryDTO 分类信息
     * @return 更新后的分类
     */
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);

    /**
     * 根据ID获取分类
     *
     * @param id 分类ID
     * @return 分类信息
     */
    CategoryDTO getCategoryById(Long id);

    /**
     * 根据父级分类ID获取子分类
     *
     * @param parentId 父级分类ID
     * @return 子分类列表
     */
    List<CategoryDTO> getCategoriesByParentId(Long parentId);

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    List<CategoryDTO> getAllCategories();

    /**
     * 获取所有启用的分类
     *
     * @return 分类列表
     */
    List<CategoryDTO> getAllEnabledCategories();

    /**
     * 获取分类列表（分页）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分类列表
     */
    List<CategoryDTO> getCategoryList(Integer page, Integer size);

    /**
     * 获取分类总数
     *
     * @return 总数
     */
    int getCategoryCount();

    /**
     * 删除分类
     *
     * @param id 分类ID
     */
    void deleteCategory(Long id);

    /**
     * 启用分类
     *
     * @param id 分类ID
     * @return 更新后的分类
     */
    CategoryDTO enableCategory(Long id);

    /**
     * 禁用分类
     *
     * @param id 分类ID
     * @return 更新后的分类
     */
    CategoryDTO disableCategory(Long id);

    /**
     * 获取分类树
     *
     * @return 分类树
     */
    List<CategoryTreeNodeDTO> getCategoryTree();

    /**
     * 获取分类路径
     *
     * @param id 分类ID
     * @return 分类路径
     */
    List<CategoryDTO> getCategoryPath(Long id);

    /**
     * 检查分类是否存在子分类
     *
     * @param id 分类ID
     * @return 是否存在子分类
     */
    boolean hasChildren(Long id);

    /**
     * 检查分类下是否有商品
     *
     * @param id 分类ID
     * @return 是否有商品
     */
    boolean hasProducts(Long id);

    /**
     * 移动分类
     *
     * @param id       分类ID
     * @param newParentId 新的父级分类ID
     * @return 移动后的分类
     */
    CategoryDTO moveCategory(Long id, Long newParentId);

    /**
     * 更新分类排序
     *
     * @param id       分类ID
     * @param sortOrder 排序值
     * @return 更新后的分类
     */
    CategoryDTO updateCategorySortOrder(Long id, Integer sortOrder);
}