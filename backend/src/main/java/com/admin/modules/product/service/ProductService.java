package com.admin.modules.product.service;

import com.admin.modules.product.dto.*;
import com.admin.modules.product.entity.*;
import com.admin.modules.product.enums.ProductStatus;
import com.admin.modules.product.repository.*;
import com.admin.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SkuRepository skuRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductAttributeRepository productAttributeRepository;
    
    public ProductDto createProduct(ProductCreateRequest request) {
        // 验证分类存在
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("分类不存在: " + request.getCategoryId()));
        
        // 验证商品编码唯一性
        if (productRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("商品编码已存在: " + request.getCode());
        }
        
        // 验证SKU编码唯一性
        request.getSkus().forEach(skuRequest -> {
            if (skuRepository.existsBySkuCode(skuRequest.getSkuCode())) {
                throw new IllegalArgumentException("SKU编码已存在: " + skuRequest.getSkuCode());
            }
        });
        
        // 创建商品
        Product product = new Product();
        product.setName(request.getName());
        product.setCode(request.getCode());
        product.setCategory(category);
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setRichContent(request.getRichContent());
        product.setStatus(ProductStatus.DRAFT);
        product.setIsDeleted(false);
        
        product = productRepository.save(product);
        
        // 创建SKU
        List<Sku> skus = request.getSkus().stream()
                .map(skuRequest -> {
                    Sku sku = new Sku();
                    sku.setProduct(product);
                    sku.setSkuCode(skuRequest.getSkuCode());
                    sku.setName(skuRequest.getName());
                    sku.setPrice(skuRequest.getPrice());
                    sku.setMarketPrice(skuRequest.getMarketPrice());
                    sku.setStock(skuRequest.getStock());
                    sku.setWarningStock(skuRequest.getWarningStock());
                    sku.setBarcode(skuRequest.getBarcode());
                    sku.setWeight(skuRequest.getWeight());
                    sku.setSpecifications(skuRequest.getSpecifications());
                    sku.setStatus(ProductStatus.DRAFT);
                    return sku;
                })
                .collect(Collectors.toList());
        
        skuRepository.saveAll(skus);
        
        // 创建商品图片
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ProductImage> images = request.getImages().stream()
                    .map(imageRequest -> {
                        ProductImage image = new ProductImage();
                        image.setProduct(product);
                        image.setUrl(imageRequest.getUrl());
                        image.setAlt(imageRequest.getAlt());
                        image.setSortOrder(imageRequest.getSortOrder());
                        image.setIsMain(imageRequest.getIsMain());
                        return image;
                    })
                    .collect(Collectors.toList());
            
            productImageRepository.saveAll(images);
        }
        
        // 创建商品属性
        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            List<ProductAttribute> attributes = request.getAttributes().stream()
                    .map(attrRequest -> {
                        ProductAttribute attribute = new ProductAttribute();
                        attribute.setProduct(product);
                        attribute.setName(attrRequest.getName());
                        attribute.setValue(attrRequest.getValue());
                        attribute.setSortOrder(attrRequest.getSortOrder());
                        attribute.setIsSaleAttribute(attrRequest.getIsSaleAttribute());
                        attribute.setGroupName(attrRequest.getGroupName());
                        return attribute;
                    })
                    .collect(Collectors.toList());
            
            productAttributeRepository.saveAll(attributes);
        }
        
        return getProductById(product.getId());
    }
    
    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商品不存在: " + id));
        
        if (product.getIsDeleted()) {
            throw new ResourceNotFoundException("商品已删除: " + id);
        }
        
        return convertToDto(product);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductDto> getProducts(Pageable pageable) {
        return productRepository.findByIsDeletedFalse(pageable)
                .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductDto> getProductsByStatus(String status, Pageable pageable) {
        return productRepository.findByIsDeletedFalseAndStatus(status, pageable)
                .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndStatusOrderByCreatedAtDesc(
                        categoryId, ProductStatus.ACTIVE.name())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public ProductDto updateProduct(Long id, ProductCreateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商品不存在: " + id));
        
        if (product.getIsDeleted()) {
            throw new ResourceNotFoundException("商品已删除: " + id);
        }
        
        // 验证商品编码唯一性
        if (!product.getCode().equals(request.getCode()) && 
            productRepository.existsByCodeAndIdNot(request.getCode(), id)) {
            throw new IllegalArgumentException("商品编码已存在: " + request.getCode());
        }
        
        // 验证SKU编码唯一性
        request.getSkus().forEach(skuRequest -> {
            if (skuRepository.existsBySkuCodeAndIdNot(skuRequest.getSkuCode(), 
                request.getSkus().indexOf(skuRequest))) {
                throw new IllegalArgumentException("SKU编码已存在: " + skuRequest.getSkuCode());
            }
        });
        
        // 更新商品基本信息
        product.setName(request.getName());
        product.setCode(request.getCode());
        product.setBrand(request.getBrand());
        product.setDescription(request.getDescription());
        product.setRichContent(request.getRichContent());
        
        // 更新分类
        if (!product.getCategory().getId().equals(request.getCategoryId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("分类不存在: " + request.getCategoryId()));
            product.setCategory(category);
        }
        
        product = productRepository.save(product);
        
        // 更新SKU
        skuRepository.deleteByProductId(product.getId());
        List<Sku> skus = request.getSkus().stream()
                .map(skuRequest -> {
                    Sku sku = new Sku();
                    sku.setProduct(product);
                    sku.setSkuCode(skuRequest.getSkuCode());
                    sku.setName(skuRequest.getName());
                    sku.setPrice(skuRequest.getPrice());
                    sku.setMarketPrice(skuRequest.getMarketPrice());
                    sku.setStock(skuRequest.getStock());
                    sku.setWarningStock(skuRequest.getWarningStock());
                    sku.setBarcode(skuRequest.getBarcode());
                    sku.setWeight(skuRequest.getWeight());
                    sku.setSpecifications(skuRequest.getSpecifications());
                    sku.setStatus(product.getStatus());
                    return sku;
                })
                .collect(Collectors.toList());
        
        skuRepository.saveAll(skus);
        
        // 更新商品图片
        productImageRepository.deleteByProductId(product.getId());
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<ProductImage> images = request.getImages().stream()
                    .map(imageRequest -> {
                        ProductImage image = new ProductImage();
                        image.setProduct(product);
                        image.setUrl(imageRequest.getUrl());
                        image.setAlt(imageRequest.getAlt());
                        image.setSortOrder(imageRequest.getSortOrder());
                        image.setIsMain(imageRequest.getIsMain());
                        return image;
                    })
                    .collect(Collectors.toList());
            
            productImageRepository.saveAll(images);
        }
        
        // 更新商品属性
        productAttributeRepository.deleteByProductId(product.getId());
        if (request.getAttributes() != null && !request.getAttributes().isEmpty()) {
            List<ProductAttribute> attributes = request.getAttributes().stream()
                    .map(attrRequest -> {
                        ProductAttribute attribute = new ProductAttribute();
                        attribute.setProduct(product);
                        attribute.setName(attrRequest.getName());
                        attribute.setValue(attrRequest.getValue());
                        attribute.setSortOrder(attrRequest.getSortOrder());
                        attribute.setIsSaleAttribute(attrRequest.getIsSaleAttribute());
                        attribute.setGroupName(attrRequest.getGroupName());
                        return attribute;
                    })
                    .collect(Collectors.toList());
            
            productAttributeRepository.saveAll(attributes);
        }
        
        return getProductById(product.getId());
    }
    
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商品不存在: " + id));
        
        product.setIsDeleted(true);
        productRepository.save(product);
        
        // 同时删除关联的SKU
        List<Sku> skus = skuRepository.findByProductId(id);
        skus.forEach(sku -> sku.setStatus(ProductStatus.INACTIVE));
        skuRepository.saveAll(skus);
    }
    
    public ProductDto updateProductStatus(Long id, String status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("商品不存在: " + id));
        
        if (product.getIsDeleted()) {
            throw new ResourceNotFoundException("商品已删除: " + id);
        }
        
        product.setStatus(ProductStatus.valueOf(status));
        product = productRepository.save(product);
        
        // 同步更新SKU状态
        List<Sku> skus = skuRepository.findByProductId(id);
        skus.forEach(sku -> sku.setStatus(product.getStatus()));
        skuRepository.saveAll(skus);
        
        return convertToDto(product);
    }
    
    @Transactional(readOnly = true)
    public Page<ProductDto> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable)
                .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public List<ProductDto> getLowStockProducts() {
        return skuRepository.findAllLowStockSkus().stream()
                .map(sku -> sku.getProduct())
                .distinct()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    private ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCode(product.getCode());
        dto.setBrand(product.getBrand());
        dto.setDescription(product.getDescription());
        dto.setRichContent(product.getRichContent());
        dto.setStatus(product.getStatus().name());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        
        // 设置分类信息
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
        }
        
        // 设置SKU列表
        List<SkuDto> skuDtos = skuRepository.findByProductId(product.getId()).stream()
                .map(this::convertSkuToDto)
                .collect(Collectors.toList());
        dto.setSkus(skuDtos);
        
        // 设置图片列表
        List<ProductImageDto> imageDtos = productImageRepository.findByProductId(product.getId()).stream()
                .map(this::convertImageToDto)
                .collect(Collectors.toList());
        dto.setImages(imageDtos);
        
        // 设置属性列表
        List<ProductAttributeDto> attributeDtos = productAttributeRepository.findByProductId(product.getId()).stream()
                .map(this::convertAttributeToDto)
                .collect(Collectors.toList());
        dto.setAttributes(attributeDtos);
        
        return dto;
    }
    
    private SkuDto convertSkuToDto(Sku sku) {
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
    
    private ProductImageDto convertImageToDto(ProductImage image) {
        ProductImageDto dto = new ProductImageDto();
        dto.setId(image.getId());
        dto.setUrl(image.getUrl());
        dto.setAlt(image.getAlt());
        dto.setSortOrder(image.getSortOrder());
        dto.setIsMain(image.getIsMain());
        return dto;
    }
    
    private ProductAttributeDto convertAttributeToDto(ProductAttribute attribute) {
        ProductAttributeDto dto = new ProductAttributeDto();
        dto.setId(attribute.getId());
        dto.setName(attribute.getName());
        dto.setValue(attribute.getValue());
        dto.setSortOrder(attribute.getSortOrder());
        dto.setIsSaleAttribute(attribute.getIsSaleAttribute());
        dto.setGroupName(attribute.getGroupName());
        return dto;
    }
}