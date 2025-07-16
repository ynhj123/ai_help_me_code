package com.admin.modules.product.service;

import com.admin.modules.product.dto.CategoryCreateRequest;
import com.admin.modules.product.dto.CategoryDto;
import com.admin.modules.product.entity.Category;
import com.admin.modules.product.enums.CategoryStatus;
import com.admin.modules.product.repository.CategoryRepository;
import com.admin.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    
    @Mock
    private CategoryRepository categoryRepository;
    
    @InjectMocks
    private CategoryService categoryService;
    
    private Category category;
    private CategoryCreateRequest request;
    
    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("电子产品");
        category.setCode("electronics");
        category.setDescription("各类电子产品");
        category.setStatus(CategoryStatus.ACTIVE);
        
        request = new CategoryCreateRequest();
        request.setName("电子产品");
        request.setCode("electronics");
        request.setDescription("各类电子产品");
        request.setSortOrder(1);
    }
    
    @Test
    void createCategory_Success() {
        when(categoryRepository.existsByCode(anyString())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        
        CategoryDto result = categoryService.createCategory(request);
        
        assertNotNull(result);
        assertEquals("电子产品", result.getName());
        assertEquals("electronics", result.getCode());
        verify(categoryRepository).save(any(Category.class));
    }
    
    @Test
    void createCategory_CodeExists_ThrowsException() {
        when(categoryRepository.existsByCode(anyString())).thenReturn(true);
        
        assertThrows(IllegalArgumentException.class, () -> {
            categoryService.createCategory(request);
        });
    }
    
    @Test
    void getCategoryById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        
        CategoryDto result = categoryService.getCategoryById(1L);
        
        assertNotNull(result);
        assertEquals("电子产品", result.getName());
    }
    
    @Test
    void getCategoryById_NotFound_ThrowsException() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.getCategoryById(1L);
        });
    }
    
    @Test
    void getAllCategories_Success() {
        when(categoryRepository.findByStatusOrderBySortOrderAsc(anyString()))
                .thenReturn(Arrays.asList(category));
        
        List<CategoryDto> result = categoryService.getAllCategories();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("电子产品", result.get(0).getName());
    }
    
    @Test
    void updateCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByCodeAndIdNot(anyString(), anyLong())).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        
        CategoryDto result = categoryService.updateCategory(1L, request);
        
        assertNotNull(result);
        assertEquals("电子产品", result.getName());
        verify(categoryRepository).save(any(Category.class));
    }
    
    @Test
    void deleteCategory_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByParentId(1L)).thenReturn(Arrays.asList());
        
        assertDoesNotThrow(() -> {
            categoryService.deleteCategory(1L);
        });
        
        verify(categoryRepository).save(any(Category.class));
    }
    
    @Test
    void deleteCategory_HasChildren_ThrowsException() {
        Category child = new Category();
        child.setId(2L);
        child.setName("手机");
        child.setParentId(1L);
        
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.findByParentId(1L)).thenReturn(Arrays.asList(child));
        
        assertThrows(IllegalStateException.class, () -> {
            categoryService.deleteCategory(1L);
        });
    }
}