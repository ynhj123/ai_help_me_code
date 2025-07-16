package com.admin.modules.product.repository;

import com.admin.modules.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByCode(String code);
    
    List<Product> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, String status);
    
    List<Product> findByStatusOrderByCreatedAtDesc(String status);
    
    Page<Product> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
    
    Page<Product> findByCategoryIdAndStatusOrderByCreatedAtDesc(Long categoryId, String status, Pageable pageable);
    
    boolean existsByCode(String code);
    
    boolean existsByCodeAndIdNot(String code, Long id);
    
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.code LIKE %:keyword% OR p.brand LIKE %:keyword%")
    Page<Product> searchProducts(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.categoryId = :categoryId AND p.name LIKE %:keyword%")
    Page<Product> searchProductsByCategory(@Param("categoryId") Long categoryId, @Param("keyword") String keyword, Pageable pageable);
    
    List<Product> findByIsDeletedFalse();
    
    List<Product> findByIsDeletedFalseAndStatus(String status);
}