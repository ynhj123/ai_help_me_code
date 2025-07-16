package com.admin.modules.logistics.repository;

import com.admin.modules.logistics.entity.ShippingTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingTrackRepository extends JpaRepository<ShippingTrack, Long> {
    
    List<ShippingTrack> findByShippingOrderIdOrderByTimestampDesc(Long shippingOrderId);
}