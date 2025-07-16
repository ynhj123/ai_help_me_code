package com.admin.modules.product.service;

import com.admin.modules.product.dto.SkuDto;
import com.admin.modules.product.entity.Sku;
import com.admin.modules.product.repository.SkuRepository;
import com.admin.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {
    
    private final SkuRepository skuRepository;
    
    public SkuDto updateStock(Long skuId, Integer quantity, String operation) {
        Sku sku = skuRepository.findById(skuId)
                .orElseThrow(() -> new ResourceNotFoundException("SKU不存在: " + skuId));
        
        switch (operation.toUpperCase()) {
            case "INCREASE":
                sku.setStock(sku.getStock() + quantity);
                break;
            case "DECREASE":
                if (sku.getStock() < quantity) {
                    throw new IllegalStateException("库存不足，当前库存: " + sku.getStock());
                }
                sku.setStock(sku.getStock() - quantity);
                break;
            case "SET":
                sku.setStock(quantity);
                break;
            default:
                throw new IllegalArgumentException("无效的操作类型: " + operation);
        }
        
        sku = skuRepository.save(sku);
        return convertToDto(sku);
    }
    
    public SkuDto updateWarningStock(Long skuId, Integer warningStock) {
        Sku sku = skuRepository.findById(skuId)
                .orElseThrow(() -> new ResourceNotFoundException("SKU不存在: " + skuId));
        
        sku.setWarningStock(warningStock);
        sku = skuRepository.save(sku);
        return convertToDto(sku);
    }
    
    @Transactional(readOnly = true)
    public List<SkuDto> getLowStockSkus() {
        return skuRepository.findAllLowStockSkus().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<SkuDto> getLowStockSkusByProduct(Long productId) {
        return skuRepository.findLowStockSkusByProduct(productId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Integer getTotalStockByProduct(Long productId) {
        return skuRepository.findByProductId(productId).stream()
                .mapToInt(Sku::getStock)
                .sum();
    }
    
    private SkuDto convertToDto(Sku sku) {
        SkuDto dto = new SkuDto();
        dto.setId(sku.getId());
        dto.setSkuCode(sku.getSkuCode());
        dto.setName(sku.getName());
        dto.setPrice(sku.getPrice());
        dto.setMarketPrice(sku.getMarketPrice());
        dto.setStock(sku.getStock());
        dto.setWarningStock(sku.getWarningStock());
        dto.setBarcode(sku.getBarcode());
        dto.setWeight(sku.getWeight());
        dto.setSpecifications(sku.getSpecifications());
        dto.setStatus(sku.getStatus().name());
        return dto;
    }
}