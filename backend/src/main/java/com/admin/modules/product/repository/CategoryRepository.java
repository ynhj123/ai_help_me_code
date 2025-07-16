package com.admin.modules.product.repository;

import com.admin.modules.product.entity.Category;
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
    
    List<Category> findByParentIdAndStatusOrderBySortOrderAsc(Long parentId, String status);
    
    List<Category> findByStatusOrderBySortOrderAsc(String status);
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    @Query("SELECT c FROM Category c WHERE c.parentId IS NULL AND c.status = :status ORDER BY c.sortOrder ASC")
    List<Category> findRootCategories(@Param("status") String status);
    
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword% OR c.code LIKE %:keyword%")
    Page<Category> searchCategories(@Param("keyword") String keyword, Pageable pageable);
    
    List<Category> findByParentId(Long parentId);
}