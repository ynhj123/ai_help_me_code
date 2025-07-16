package com.admin.modules.product.controller;

import com.admin.modules.product.dto.CategoryCreateRequest;
import com.admin.modules.product.dto.CategoryDto;
import com.admin.modules.product.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "商品分类管理", description = "商品分类相关接口")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController {
    
    private final CategoryService categoryService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "创建商品分类")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryCreateRequest request) {
        CategoryDto category = categoryService.createCategory(request);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取商品分类详情")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    
    @GetMapping
    @Operation(summary = "获取所有商品分类")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/tree")
    @Operation(summary = "获取商品分类树")
    public ResponseEntity<List<CategoryDto>> getCategoryTree() {
        List<CategoryDto> tree = categoryService.getCategoryTree();
        return ResponseEntity.ok(tree);
    }
    
    @GetMapping("/parent/{parentId}")
    @Operation(summary = "根据父分类ID获取子分类")
    public ResponseEntity<List<CategoryDto>> getCategoriesByParent(@PathVariable Long parentId) {
        List<CategoryDto> categories = categoryService.getCategoriesByParentId(parentId);
        return ResponseEntity.ok(categories);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "更新商品分类")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryCreateRequest request) {
        CategoryDto category = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(category);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "删除商品分类")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "切换商品分类状态")
    public ResponseEntity<Void> toggleCategoryStatus(@PathVariable Long id) {
        categoryService.toggleCategoryStatus(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "搜索商品分类")
    public ResponseEntity<Page<CategoryDto>> searchCategories(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<CategoryDto> categories = categoryService.searchCategories(
                keyword != null ? keyword : "", pageable);
        return ResponseEntity.ok(categories);
    }
}