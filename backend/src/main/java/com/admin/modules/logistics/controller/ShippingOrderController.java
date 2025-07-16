package com.admin.modules.logistics.controller;

import com.admin.modules.logistics.dto.ShippingOrderCreateRequest;
import com.admin.modules.logistics.dto.ShippingOrderDto;
import com.admin.modules.logistics.dto.ShippingTrackDto;
import com.admin.modules.logistics.enums.ShippingStatus;
import com.admin.modules.logistics.service.ShippingOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logistics/shipping")
@RequiredArgsConstructor
public class ShippingOrderController {
    
    private final ShippingOrderService shippingOrderService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<ShippingOrderDto> createShippingOrder(@Valid @RequestBody ShippingOrderCreateRequest request) {
        ShippingOrderDto shippingOrder = shippingOrderService.createShippingOrder(request);
        return ResponseEntity.ok(shippingOrder);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER') or hasRole('USER')")
    public ResponseEntity<ShippingOrderDto> getShippingOrder(@PathVariable Long id) {
        ShippingOrderDto shippingOrder = shippingOrderService.getShippingOrderById(id);
        return ResponseEntity.ok(shippingOrder);
    }
    
    @GetMapping("/tracking/{trackingNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER') or hasRole('USER')")
    public ResponseEntity<ShippingOrderDto> getShippingOrderByTrackingNumber(@PathVariable String trackingNumber) {
        ShippingOrderDto shippingOrder = shippingOrderService.getShippingOrderByTrackingNumber(trackingNumber);
        return ResponseEntity.ok(shippingOrder);
    }
    
    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<ShippingOrderDto>> getShippingOrdersByOrderId(@PathVariable Long orderId) {
        List<ShippingOrderDto> shippingOrders = shippingOrderService.getShippingOrdersByOrderId(orderId);
        return ResponseEntity.ok(shippingOrders);
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER') or @userService.getCurrentUser().id == #userId")
    public ResponseEntity<Page<ShippingOrderDto>> getShippingOrdersByUserId(
            @PathVariable Long userId,
            Pageable pageable) {
        Page<ShippingOrderDto> shippingOrders = shippingOrderService.getShippingOrdersByUserId(userId, pageable);
        return ResponseEntity.ok(shippingOrders);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<Page<ShippingOrderDto>> getAllShippingOrders(Pageable pageable) {
        Page<ShippingOrderDto> shippingOrders = shippingOrderService.getAllShippingOrders(pageable);
        return ResponseEntity.ok(shippingOrders);
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<ShippingOrderDto> updateShippingStatus(
            @PathVariable Long id,
            @RequestParam ShippingStatus status,
            @RequestParam(required = false) String notes) {
        ShippingOrderDto shippingOrder = shippingOrderService.updateShippingStatus(id, status, notes);
        return ResponseEntity.ok(shippingOrder);
    }
    
    @PostMapping("/{id}/track")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<ShippingTrackDto> addTrackRecord(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam(required = false) String notes) {
        ShippingTrackDto track = shippingOrderService.addTrackRecord(id, status, description, location, notes);
        return ResponseEntity.ok(track);
    }
}