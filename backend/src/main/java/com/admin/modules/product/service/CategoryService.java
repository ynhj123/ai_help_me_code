package com.admin.modules.product.service;

import com.admin.modules.product.dto.CategoryCreateRequest;
import com.admin.modules.product.dto.CategoryDto;
import com.admin.modules.product.entity.Category;
import com.admin.modules.product.enums.CategoryStatus;
import com.admin.modules.product.repository.CategoryRepository;
import com.admin.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public CategoryDto createCategory(CategoryCreateRequest request) {
        if (categoryRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("分类编码已存在: " + request.getCode());
        }
        
        Category category = new Category();
        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setSortOrder(request.getSortOrder());
        category.setParentId(request.getParentId());
        category.setStatus(CategoryStatus.ACTIVE);
        
        category = categoryRepository.save(category);
        return convertToDto(category);
    }
    
    @Transactional(readOnly = true)
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在: " + id));
        return convertToDto(category);
    }
    
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findByStatusOrderBySortOrderAsc(CategoryStatus.ACTIVE.name())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoryTree() {
        List<Category> rootCategories = categoryRepository.findRootCategories(CategoryStatus.ACTIVE.name());
        return rootCategories.stream()
                .map(this::buildCategoryTree)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByParentId(Long parentId) {
        return categoryRepository.findByParentIdAndStatusOrderBySortOrderAsc(parentId, CategoryStatus.ACTIVE.name())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public CategoryDto updateCategory(Long id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在: " + id));
        
        if (categoryRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new IllegalArgumentException("分类编码已存在: " + request.getCode());
        }
        
        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        category.setSortOrder(request.getSortOrder());
        category.setParentId(request.getParentId());
        
        category = categoryRepository.save(category);
        return convertToDto(category);
    }
    
    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在: " + id));
        
        // 检查是否有子分类
        List<Category> children = categoryRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new IllegalStateException("该分类下存在子分类，无法删除");
        }
        
        category.setStatus(CategoryStatus.INACTIVE);
        categoryRepository.save(category);
    }
    
    public void toggleCategoryStatus(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在: " + id));
        
        if (CategoryStatus.ACTIVE.equals(category.getStatus())) {
            category.setStatus(CategoryStatus.INACTIVE);
        } else {
            category.setStatus(CategoryStatus.ACTIVE);
        }
        
        categoryRepository.save(category);
    }
    
    @Transactional(readOnly = true)
    public Page<CategoryDto> searchCategories(String keyword, Pageable pageable) {
        return categoryRepository.searchCategories(keyword, pageable)
                .map(this::convertToDto);
    }
    
    private CategoryDto buildCategoryTree(Category category) {
        CategoryDto dto = convertToDto(category);
        List<Category> children = categoryRepository.findByParentIdAndStatusOrderBySortOrderAsc(
                category.getId(), CategoryStatus.ACTIVE.name());
        
        if (!children.isEmpty()) {
            List<CategoryDto> childrenDtos = children.stream()
                    .map(this::buildCategoryTree)
                    .collect(Collectors.toList());
            dto.setChildren(childrenDtos);
        }
        
        return dto;
    }
    
    private CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setCode(category.getCode());
        dto.setDescription(category.getDescription());
        dto.setImageUrl(category.getImageUrl());
        dto.setSortOrder(category.getSortOrder());
        dto.setParentId(category.getParentId());
        dto.setStatus(category.getStatus().name());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        
        if (category.getParentId() != null) {
            categoryRepository.findById(category.getParentId())
                    .ifPresent(parent -> dto.setParentName(parent.getName()));
        }
        
        return dto;
    }
}