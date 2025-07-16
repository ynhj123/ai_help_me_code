package com.admin.modules.logistics.controller;

import com.admin.modules.logistics.dto.CarrierCreateRequest;
import com.admin.modules.logistics.dto.CarrierDto;
import com.admin.modules.logistics.service.CarrierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logistics/carriers")
@RequiredArgsConstructor
public class CarrierController {
    
    private final CarrierService carrierService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<CarrierDto> createCarrier(@Valid @RequestBody CarrierCreateRequest request) {
        CarrierDto carrier = carrierService.createCarrier(request);
        return ResponseEntity.ok(carrier);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER') or hasRole('USER')")
    public ResponseEntity<CarrierDto> getCarrier(@PathVariable Long id) {
        CarrierDto carrier = carrierService.getCarrierById(id);
        return ResponseEntity.ok(carrier);
    }
    
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER') or hasRole('USER')")
    public ResponseEntity<List<CarrierDto>> getActiveCarriers() {
        List<CarrierDto> carriers = carrierService.getActiveCarriers();
        return ResponseEntity.ok(carriers);
    }
    
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<Page<CarrierDto>> getAllCarriers(Pageable pageable) {
        Page<CarrierDto> carriers = carrierService.getAllCarriers(pageable);
        return ResponseEntity.ok(carriers);
    }
    
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<Page<CarrierDto>> searchCarriers(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<CarrierDto> carriers = carrierService.searchCarriers(keyword, pageable);
        return ResponseEntity.ok(carriers);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<CarrierDto> updateCarrier(
            @PathVariable Long id,
            @Valid @RequestBody CarrierCreateRequest request) {
        CarrierDto carrier = carrierService.updateCarrier(id, request);
        return ResponseEntity.ok(carrier);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<Void> deleteCarrier(@PathVariable Long id) {
        carrierService.deleteCarrier(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/toggle-status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LOGISTICS_MANAGER')")
    public ResponseEntity<CarrierDto> toggleCarrierStatus(@PathVariable Long id) {
        CarrierDto carrier = carrierService.toggleCarrierStatus(id);
        return ResponseEntity.ok(carrier);
    }
}