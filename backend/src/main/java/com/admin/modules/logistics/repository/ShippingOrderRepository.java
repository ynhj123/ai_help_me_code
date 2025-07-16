package com.admin.modules.logistics.repository;

import com.admin.modules.logistics.entity.ShippingOrder;
import com.admin.modules.logistics.enums.ShippingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShippingOrderRepository extends JpaRepository<ShippingOrder, Long> {
    
    Optional<ShippingOrder> findByTrackingNumber(String trackingNumber);
    
    List<ShippingOrder> findByOrderId(Long orderId);
    
    List<ShippingOrder> findByUserId(Long userId);
    
    Page<ShippingOrder> findByUserIdAndIsDeletedFalse(Long userId, Pageable pageable);
    
    Page<ShippingOrder> findByStatusAndIsDeletedFalse(ShippingStatus status, Pageable pageable);
    
    @Query("SELECT s FROM ShippingOrder s WHERE s.isDeleted = false AND " +
           "(LOWER(s.trackingNumber) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.receiverName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.receiverPhone) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ShippingOrder> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}