package com.admin.modules.product.repository;

import com.admin.modules.product.entity.Category;
import com.admin.modules.product.entity.Category.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByCode(String code);
    
    List<Category> findByParentIdAndStatusOrderBySortOrderAsc(Long parentId, CategoryStatus status);
    
    List<Category> findByStatusOrderBySortOrderAsc(CategoryStatus status);
    
    List<Category> findAllByOrderBySortOrderAsc();
    
    List<Category> findByParentIsNullOrderBySortOrderAsc();
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.status = :status ORDER BY c.sortOrder ASC")
    List<Category> findRootCategories(@Param("status") CategoryStatus status);
    
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword% OR c.code LIKE %:keyword%")
    Page<Category> searchCategories(@Param("keyword") String keyword, Pageable pageable);
    
    List<Category> findByParentId(Long parentId);
}