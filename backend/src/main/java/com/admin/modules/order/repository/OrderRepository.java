package com.admin.modules.order.repository;

import com.admin.modules.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    Optional<Order> findByOrderNumberAndIsDeletedFalse(String orderNumber);
    
    Optional<Order> findByIdAndIsDeletedFalse(Long id);
    
    List<Order> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId);
    
    Page<Order> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    List<Order> findByIsDeletedFalseOrderByCreatedAtDesc();
    
    Page<Order> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.userId = :userId AND o.isDeleted = false ORDER BY o.createdAt DESC")
    List<Order> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT o FROM Order o WHERE o.isDeleted = false ORDER BY o.createdAt DESC")
    Page<Order> findAllActiveOrders(Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.orderNumber LIKE %:keyword% OR o.user.username LIKE %:keyword%")
    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);
}