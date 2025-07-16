package com.admin.modules.product.controller;

import com.admin.modules.product.dto.SkuDto;
import com.admin.modules.product.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Tag(name = "库存管理", description = "库存相关接口")
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    @PutMapping("/sku/{skuId}/stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "更新库存")
    public ResponseEntity<SkuDto> updateStock(
            @PathVariable Long skuId,
            @RequestParam Integer quantity,
            @RequestParam String operation) {
        SkuDto sku = inventoryService.updateStock(skuId, quantity, operation);
        return ResponseEntity.ok(sku);
    }
    
    @PutMapping("/sku/{skuId}/warning-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "更新预警库存")
    public ResponseEntity<SkuDto> updateWarningStock(
            @PathVariable Long skuId,
            @RequestParam Integer warningStock) {
        SkuDto sku = inventoryService.updateWarningStock(skuId, warningStock);
        return ResponseEntity.ok(sku);
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "获取低库存SKU列表")
    public ResponseEntity<List<SkuDto>> getLowStockSkus() {
        List<SkuDto> skus = inventoryService.getLowStockSkus();
        return ResponseEntity.ok(skus);
    }
    
    @GetMapping("/product/{productId}/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "获取指定商品的低库存SKU列表")
    public ResponseEntity<List<SkuDto>> getLowStockSkusByProduct(@PathVariable Long productId) {
        List<SkuDto> skus = inventoryService.getLowStockSkusByProduct(productId);
        return ResponseEntity.ok(skus);
    }
    
    @GetMapping("/product/{productId}/total-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "获取商品总库存")
    public ResponseEntity<Integer> getTotalStockByProduct(@PathVariable Long productId) {
        Integer totalStock = inventoryService.getTotalStockByProduct(productId);
        return ResponseEntity.ok(totalStock);
    }
}