package com.admin.modules.logistics.repository;

import com.admin.modules.logistics.entity.Carrier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    
    Optional<Carrier> findByCode(String code);
    
    List<Carrier> findByIsActiveTrueAndIsDeletedFalse();
    
    Page<Carrier> findByIsDeletedFalse(Pageable pageable);
    
    @Query("SELECT c FROM Carrier c WHERE c.isDeleted = false AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Carrier> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}