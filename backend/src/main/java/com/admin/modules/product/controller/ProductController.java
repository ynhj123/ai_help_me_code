package com.admin.modules.product.controller;

import com.admin.modules.product.dto.ProductCreateRequest;
import com.admin.modules.product.dto.ProductDto;
import com.admin.modules.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "商品管理", description = "商品相关接口")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    
    private final ProductService productService;
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "创建商品")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        ProductDto product = productService.createProduct(request);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping
    @Operation(summary = "获取商品列表")
    public ResponseEntity<Page<ProductDto>> getProducts(Pageable pageable) {
        Page<ProductDto> products = productService.getProducts(pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/status/{status}")
    @Operation(summary = "根据状态获取商品列表")
    public ResponseEntity<Page<ProductDto>> getProductsByStatus(
            @PathVariable String status,
            Pageable pageable) {
        Page<ProductDto> products = productService.getProductsByStatus(status, pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类获取商品列表")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductDto> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "更新商品")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductCreateRequest request) {
        ProductDto product = productService.updateProduct(id, request);
        return ResponseEntity.ok(product);
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "删除商品")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "更新商品状态")
    public ResponseEntity<ProductDto> updateProductStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        ProductDto product = productService.updateProductStatus(id, status);
        return ResponseEntity.ok(product);
    }
    
    @GetMapping("/search")
    @Operation(summary = "搜索商品")
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<ProductDto> products = productService.searchProducts(
                keyword != null ? keyword : "", pageable);
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/low-stock")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCT_MANAGER')")
    @Operation(summary = "获取低库存商品列表")
    public ResponseEntity<List<ProductDto>> getLowStockProducts() {
        List<ProductDto> products = productService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }
}