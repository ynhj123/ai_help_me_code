package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductQueryRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductInventory;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.ProductInventoryMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品服务实现类
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductInventoryMapper productInventoryMapper;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        // 检查SKU是否已存在
        if (productDTO.getSku() != null && !productDTO.getSku().isEmpty()) {
            Product existingProduct = productMapper.selectBySku(productDTO.getSku());
            if (existingProduct != null) {
                throw new BusinessException(400, "SKU已存在");
            }
        }

        // 创建商品
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        product.setStatus(ProductStatus.ACTIVE);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        // 保存商品
        productMapper.insert(product);

        // 初始化库存
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(product.getId());
        inventory.setQuantity(0);
        inventory.setReservedQuantity(0);
        inventory.setCreatedAt(LocalDateTime.now());
        inventory.setUpdatedAt(LocalDateTime.now());
        productInventoryMapper.insert(inventory);

        // 转换为DTO
        ProductDTO createdProductDTO = new ProductDTO();
        BeanUtils.copyProperties(product, createdProductDTO);
        return createdProductDTO;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        // 查询商品
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 检查SKU是否已存在
        if (productDTO.getSku() != null && !productDTO.getSku().isEmpty() 
                && !productDTO.getSku().equals(product.getSku())) {
            Product existingProduct = productMapper.selectBySku(productDTO.getSku());
            if (existingProduct != null) {
                throw new BusinessException(400, "SKU已存在");
            }
        }

        // 更新商品
        BeanUtils.copyProperties(productDTO, product, "id", "createdAt", "status");
        product.setUpdatedAt(LocalDateTime.now());

        // 保存商品
        productMapper.update(product);

        // 转换为DTO
        ProductDTO updatedProductDTO = new ProductDTO();
        BeanUtils.copyProperties(product, updatedProductDTO);
        return updatedProductDTO;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        // 查询商品
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 转换为DTO
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    @Override
    public ProductDTO getProductBySku(String sku) {
        // 查询商品
        Product product = productMapper.selectBySku(sku);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 转换为DTO
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    @Override
    public List<ProductDTO> getProductList(ProductQueryRequest queryRequest) {
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
        return productList.stream().map(product -> {
            ProductDTO productDTO = new ProductDTO();
            BeanUtils.copyProperties(product, productDTO);
            return productDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public int getProductCount(ProductQueryRequest queryRequest) {
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
        // 查询商品
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 删除商品
        productMapper.deleteById(id);

        // 删除库存
        productInventoryMapper.deleteByProductId(id);
    }

    @Override
    @Transactional
    public ProductDTO enableProduct(Long id) {
        // 查询商品
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 上架商品
        product.setStatus(ProductStatus.ACTIVE);
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.update(product);

        // 转换为DTO
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    @Override
    @Transactional
    public ProductDTO disableProduct(Long id) {
        // 查询商品
        Product product = productMapper.selectById(id);
        if (product == null) {
            throw new BusinessException(404, "商品不存在");
        }

        // 下架商品
        product.setStatus(ProductStatus.INACTIVE);
        product.setUpdatedAt(LocalDateTime.now());
        productMapper.update(product);

        // 转换为DTO
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(product, productDTO);
        return productDTO;
    }

    @Override
    public ProductInventory getProductInventory(Long productId) {
        return productInventoryMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    public ProductInventory updateProductInventory(Long productId, Integer quantity) {
        // 查询库存
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        if (inventory == null) {
            throw new BusinessException(404, "商品库存不存在");
        }

        // 更新库存
        inventory.setQuantity(quantity);
        inventory.setUpdatedAt(LocalDateTime.now());
        productInventoryMapper.update(inventory);

        return inventory;
    }

    @Override
    @Transactional
    public ProductInventory increaseProductInventory(Long productId, Integer quantity) {
        // 查询库存
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        if (inventory == null) {
            throw new BusinessException(404, "商品库存不存在");
        }

        // 增加库存
        productInventoryMapper.increaseQuantity(productId, quantity);

        // 重新查询库存
        return productInventoryMapper.selectByProductId(productId);
    }

    @Override
    @Transactional
    public ProductInventory decreaseProductInventory(Long productId, Integer quantity) {
        // 查询库存
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        if (inventory == null) {
            throw new BusinessException(404, "商品库存不存在");
        }

        // 减少库存
        int affectedRows = productInventoryMapper.decreaseQuantity(productId, quantity);
        if (affectedRows == 0) {
            throw new BusinessException(400, "库存不足");
        }

        // 重新查询库存
        return productInventoryMapper.selectByProductId(productId);
    }

    @Override
    public boolean checkInventoryStock(Long productId, Integer quantity) {
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        return inventory != null && inventory.getQuantity() >= quantity;
    }

    @Override
    @Transactional
    public boolean reserveInventory(Long productId, Integer quantity) {
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
        ProductInventory inventory = productInventoryMapper.selectByProductId(productId);
        if (inventory != null) {
            inventory.setReservedQuantity(Math.max(0, inventory.getReservedQuantity() - quantity));
            inventory.setUpdatedAt(LocalDateTime.now());
            productInventoryMapper.update(inventory);
        }
    }
}