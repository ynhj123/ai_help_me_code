package com.admin.modules.product.repository;

import com.admin.modules.product.entity.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkuRepository extends JpaRepository<Sku, Long> {
    
    Optional<Sku> findBySkuCode(String skuCode);
    
    List<Sku> findByProductId(Long productId);
    
    List<Sku> findByProductIdAndStatus(Long productId, String status);
    
    boolean existsBySkuCode(String skuCode);
    
    boolean existsBySkuCodeAndIdNot(String skuCode, Long id);
    
    List<Sku> findByStockLessThanEqual(Integer stock);
    
    @Query("SELECT s FROM Sku s WHERE s.productId = :productId AND s.stock <= s.warningStock")
    List<Sku> findLowStockSkusByProduct(@Param("productId") Long productId);
    
    @Query("SELECT s FROM Sku s WHERE s.stock <= s.warningStock")
    List<Sku> findAllLowStockSkus();
}