package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductQueryRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductInventory;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.ProductInventoryMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.service.BaseService;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品服务实现类
 * 继承BaseService，使用统一的CRUD模板方法和工具类
 */
@Service
public class ProductServiceImpl extends BaseService<Product, ProductDTO> implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    // ================ BaseService必须实现的方法 ================

    @Override
    protected ProductMapper getMapper() {
        return productMapper;
    }

    @Override
    protected Class<Product> getEntityClass() {
        return Product.class;
    }

    @Override
    protected Class<ProductDTO> getDTOClass() {
        return ProductDTO.class;
    }

    @Override
    protected String getEntityName() {
        return "商品";
    }

    @Override
    protected void doCreate(Product entity) {
        productMapper.insert(entity);
        
        // 初始化库存
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(entity.getId());
        inventory.setQuantity(0);
        inventory.setReservedQuantity(0);
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        productInventoryMapper.insert(inventory);
    }

    @Override
    protected Product doGetById(Long id) {
        return productMapper.selectById(id);
    }

    @Override
    protected void doUpdate(Product entity) {
        productMapper.update(entity);
    }

    @Override
    protected void doDelete(Long id) {
        productMapper.deleteById(id);
        // 删除库存
        productInventoryMapper.deleteByProductId(id);
    }

    @Override
    protected List<Product> doList(int offset, int limit) {
        return productMapper.selectAll(offset, limit, null, null, null, null, null, null);
    }

    @Override
    protected void validateBeforeCreate(Product entity) {
        // 检查SKU是否已存在
        if (entity.getSku() != null && !entity.getSku().isEmpty()) {
            Product existingProduct = productMapper.selectBySku(entity.getSku());
            entityValidator.validateEntityUnique(existingProduct != null, "SKU已存在");
        }
        
        // 设置默认状态
        if (entity.getStatus() == null) {
            entity.setStatus(ProductStatus.ACTIVE);
        }
    }

    @Override
    protected void validateBeforeUpdate(Long id, ProductDTO dto, Product existingEntity) {
        // 检查SKU是否已存在（排除当前商品）
        if (dto.getSku() != null && !dto.getSku().isEmpty() 
                && !dto.getSku().equals(existingEntity.getSku())) {
            Product existingProduct = productMapper.selectBySku(dto.getSku());
            entityValidator.validateEntityUnique(existingProduct != null, "SKU已存在");
        }
    }

    // ================ ProductService接口实现 ================

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        return create(productDTO);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        return update(id, productDTO);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return getById(id);
    }

    @Override
    public ProductDTO getProductBySku(String sku) {
        // 验证SKU不为空
        entityValidator.validateStringNotEmpty(sku, "SKU");
        
        // 查询商品
        Product product = productMapper.selectBySku(sku);
        entityValidator.validateEntityExists(product, "商品");

        // 转换返回DTO
        return dtoConverter.toDTO(product, ProductDTO.class);
    }

    @Override
    public List<ProductDTO> getProductList(ProductQueryRequest queryRequest) {
        // 验证查询参数
        entityValidator.validateEntityNotNull(queryRequest, "查询参数");
        entityValidator.validatePositiveNumber(queryRequest.getPage(), "页码");
        entityValidator.validatePositiveNumber(queryRequest.getSize(), "页大小");
        
        // 计算偏移量
        int offset = (queryRequest.getPage() - 1) * queryRequest.getSize();

        // 查询商品列表
        List<Product> productList = productMapper.selectAll(
                offset, 
                queryRequest.getSize(),
                queryRequest.getName(),
                queryRequest.getCategoryId(),
                queryRequest.getSku(),
                queryRequest.getStatus(),
                queryRequest.getSortBy(),
                queryRequest.getSortOrder()
        );

        // 转换为DTO列表
        return dtoConverter.toDTOList(productList, ProductDTO.class);
    }

    @Override
    public int getProductCount(ProductQueryRequest queryRequest) {
        // 验证查询参数
        entityValidator.validateEntityNotNull(queryRequest, "查询参数");
        
        return productMapper.count(
                queryRequest.getName(),
                queryRequest.getCategoryId(),
                queryRequest.getSku(),
                queryRequest.getStatus()
        );
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        delete(id);
    }

    @Override
    @Transactional
    public ProductDTO enableProduct(Long id) {
        return updateProductStatus(id, ProductStatus.ACTIVE);
    }

    @Override
    @Transactional
    public ProductDTO disableProduct(Long id) {
        return updateProductStatus(id, ProductStatus.INACTIVE);
    }
    
    /**
     * 更新商品状态的通用方法
     */
    private ProductDTO updateProductStatus(Long id, ProductStatus status) {
        // 验证ID有效性
        entityValidator.validateValidId(id, "商品ID");
        
        // 查询商品
        Product product = productMapper.selectById(id);
        entityValidator.validateEntityExists(product, "商品");

        // 更新状态
        product.setStatus(status);
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.update(product);

        // 转换返回DTO
        return dtoConverter.toDTO(product, ProductDTO.class);
    }

    @Override
    public ProductInventory getProductInventory(Long productId) {
        entityValidator.validateValidId(productId, "商品ID");
        return productInventoryMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    public ProductInventory updateProductInventory(Long productId, Integer quantity) {
        // 验证参数
        entityValidator.validateValidId(productId, "商品ID");
        entityValidator.validateNonNegativeNumber(quantity, "库存数量");
        
        // 查询库存
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        entityValidator.validateEntityExists(inventory, "商品库存");

        // 更新库存
        inventory.setQuantity(quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        productInventoryMapper.update(inventory);

        return inventory;
    }

    @Override
    @Transactional
    public ProductInventory increaseProductInventory(Long productId, Integer quantity) {
        // 验证参数
        entityValidator.validateValidId(productId, "商品ID");
        entityValidator.validatePositiveNumber(quantity, "增加数量");
        
        // 查询库存
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        entityValidator.validateEntityExists(inventory, "商品库存");

        // 增加库存
        productInventoryMapper.increaseQuantity(productId, quantity);

        // 重新查询库存
        return productInventoryMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    public ProductInventory decreaseProductInventory(Long productId, Integer quantity) {
        // 验证参数
        entityValidator.validateValidId(productId, "商品ID");
        entityValidator.validatePositiveNumber(quantity, "减少数量");
        
        // 查询库存
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        entityValidator.validateEntityExists(inventory, "商品库存");

        // 减少库存
        int affectedRows = productInventoryMapper.decreaseQuantity(productId, quantity);
        entityValidator.validateCondition(affectedRows > 0, "库存不足");

        // 重新查询库存
        return productInventoryMapper.selectByProductId(productId);
    }

    @Override
    public boolean checkInventoryStock(Long productId, Integer quantity) {
        entityValidator.validateValidId(productId, "商品ID");
        entityValidator.validatePositiveNumber(quantity, "检查数量");
        
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        return inventory != null && inventory.getQuantity() >= quantity;
    }

    @Override
    @Transactional
    public boolean reserveInventory(Long productId, Integer quantity) {
        entityValidator.validateValidId(productId, "商品ID");
        entityValidator.validatePositiveNumber(quantity, "预留数量");
        
        // 检查库存是否充足
        if (!checkInventoryStock(productId, quantity)) {
            return false;
        }

        // 预留库存
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        productInventoryMapper.update(inventory);

        return true;
    }

    @Override
    @Transactional
    public void releaseInventory(Long productId, Integer quantity) {
        entityValidator.validateValidId(productId, "商品ID");
        entityValidator.validatePositiveNumber(quantity, "释放数量");
        
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        if (inventory != null) {
            inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
            inventory.setUpdatedAt(LocalDateTime.now());
            productInventoryMapper.update(inventory);
        }
    }
}