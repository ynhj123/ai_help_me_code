package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.dto.CategoryTreeNodeDTO;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * 分类控制器
 */
@RestController
@RequestMapping("/api/categories")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取分类列表
     *
     * @param page 页码
     * @param size 每页大小
     * @return 分类列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<CategoryDTO>> getCategoryList(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        List<CategoryDTO> categoryList = categoryService.getCategoryList(page, size);
        return ResultVO.success(categoryList);
    }

    /**
     * 获取分类总数
     *
     * @return 总数
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> getCategoryCount() {
        int count = categoryService.getCategoryCount();
        return ResultVO.success(count);
    }

    /**
     * 获取分类详情
     *
     * @param id 分类ID
     * @return 分类详情
     */
    @GetMapping("/{id}")
    public ResultVO<CategoryDTO> getCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.getCategoryById(id);
        return ResultVO.success(categoryDTO);
    }

    /**
     * 根据父级分类ID获取子分类
     *
     * @param parentId 父级分类ID
     * @return 子分类列表
     */
    @GetMapping("/parent/{parentId}")
    public ResultVO<List<CategoryDTO>> getCategoriesByParentId(@PathVariable Long parentId) {
        List<CategoryDTO> categoryList = categoryService.getCategoriesByParentId(parentId);
        return ResultVO.success(categoryList);
    }

    /**
     * 获取所有分类
     *
     * @return 分类列表
     */
    @GetMapping("/all")
    public ResultVO<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categoryList = categoryService.getAllCategories();
        return ResultVO.success(categoryList);
    }

    /**
     * 获取所有启用的分类
     *
     * @return 分类列表
     */
    @GetMapping("/enabled")
    public ResultVO<List<CategoryDTO>> getAllEnabledCategories() {
        List<CategoryDTO> categoryList = categoryService.getAllEnabledCategories();
        return ResultVO.success(categoryList);
    }

    /**
     * 创建分类
     *
     * @param categoryDTO 分类信息
     * @return 创建的分类
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
        return ResultVO.success("分类创建成功", createdCategory);
    }

    /**
     * 更新分类
     *
     * @param id          分类ID
     * @param categoryDTO 分类信息
     * @return 更新后的分类
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<CategoryDTO> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
        return ResultVO.success("分类更新成功", updatedCategory);
    }

    /**
     * 删除分类
     *
     * @param id 分类ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResultVO.success("分类删除成功", null);
    }

    /**
     * 启用分类
     *
     * @param id 分类ID
     * @return 更新后的分类
     */
    @PutMapping("/{id}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<CategoryDTO> enableCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.enableCategory(id);
        return ResultVO.success("分类启用成功", categoryDTO);
    }

    /**
     * 禁用分类
     *
     * @param id 分类ID
     * @return 更新后的分类
     */
    @PutMapping("/{id}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<CategoryDTO> disableCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.disableCategory(id);
        return ResultVO.success("分类禁用成功", categoryDTO);
    }

    /**
     * 获取分类树
     *
     * @return 分类树
     */
    @GetMapping("/tree")
    public ResultVO<List<CategoryTreeNodeDTO>> getCategoryTree() {
        List<CategoryTreeNodeDTO> categoryTree = categoryService.getCategoryTree();
        return ResultVO.success(categoryTree);
    }

    /**
     * 获取分类路径
     *
     * @param id 分类ID
     * @return 分类路径
     */
    @GetMapping("/{id}/path")
    public ResultVO<List<CategoryDTO>> getCategoryPath(@PathVariable Long id) {
        List<CategoryDTO> categoryPath = categoryService.getCategoryPath(id);
        return ResultVO.success(categoryPath);
    }

    /**
     * 检查分类是否存在子分类
     *
     * @param id 分类ID
     * @return 是否存在子分类
     */
    @GetMapping("/{id}/has-children")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> hasChildren(@PathVariable Long id) {
        boolean hasChildren = categoryService.hasChildren(id);
        return ResultVO.success(hasChildren);
    }

    /**
     * 检查分类下是否有商品
     *
     * @param id 分类ID
     * @return 是否有商品
     */
    @GetMapping("/{id}/has-products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> hasProducts(@PathVariable Long id) {
        boolean hasProducts = categoryService.hasProducts(id);
        return ResultVO.success(hasProducts);
    }

    /**
     * 移动分类
     *
     * @param id          分类ID
     * @param newParentId 新的父级分类ID
     * @return 移动后的分类
     */
    @PutMapping("/{id}/move")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<CategoryDTO> moveCategory(
            @PathVariable Long id, 
            @RequestParam(required = false) Long newParentId) {
        CategoryDTO categoryDTO = categoryService.moveCategory(id, newParentId);
        return ResultVO.success("分类移动成功", categoryDTO);
    }

    /**
     * 更新分类排序
     *
     * @param id          分类ID
     * @param sortOrder   排序值
     * @return 更新后的分类
     */
    @PutMapping("/{id}/sort-order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<CategoryDTO> updateCategorySortOrder(
            @PathVariable Long id, 
            @RequestParam Integer sortOrder) {
        CategoryDTO categoryDTO = categoryService.updateCategorySortOrder(id, sortOrder);
        return ResultVO.success("分类排序更新成功", categoryDTO);
    }
}