package com.example.ecommerce.service;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductQueryRequest;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.entity.ProductInventory;
import com.example.ecommerce.enums.ProductStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.ProductInventoryMapper;
import com.example.ecommerce.mapper.ProductMapper;
import com.example.ecommerce.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ProductService的测试类
 */
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductInventoryMapper productInventoryMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct_Success() {
        // 准备测试数据
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setSku("TEST-001");
        productDTO.setPrice(100.0);
        productDTO.setDescription("Test Description");

        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setSku("TEST-001");
        product.setPrice(100.0);
        product.setDescription("Test Description");
        product.setStatus(ProductStatus.ACTIVE);

        // 配置mock行为
        when(productMapper.selectBySku("TEST-001")).thenReturn(null);
        when(productMapper.insert(any(Product.class))).thenReturn(1);

        // 执行被测方法
        ProductDTO createdProductDTO = productService.createProduct(productDTO);

        // 验证结果
        assertNotNull(createdProductDTO);
        verify(productMapper).selectBySku("TEST-001");
        verify(productMapper).insert(any(Product.class));
        verify(productInventoryMapper).insert(any(ProductInventory.class));
    }

    @Test
    void testCreateProduct_SkuExists() {
        // 准备测试数据
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setSku("EXISTING-SKU");
        productDTO.setPrice(100.0);

        Product existingProduct = new Product();
        existingProduct.setId(1L);
        existingProduct.setSku("EXISTING-SKU");

        // 配置mock行为
        when(productMapper.selectBySku("EXISTING-SKU")).thenReturn(existingProduct);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.createProduct(productDTO);
        });

        // 验证结果
        assertEquals(400, exception.getCode());
        assertEquals("SKU已存在", exception.getMessage());
        verify(productMapper).selectBySku("EXISTING-SKU");
        verify(productMapper, never()).insert(any(Product.class));
        verify(productInventoryMapper, never()).insert(any(ProductInventory.class));
    }

    @Test
    void testGetProductById_Success() {
        // 准备测试数据
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setSku("TEST-001");
        product.setPrice(100.0);

        // 配置mock行为
        when(productMapper.selectById(productId)).thenReturn(product);

        // 执行被测方法
        ProductDTO productDTO = productService.getProductById(productId);

        // 验证结果
        assertNotNull(productDTO);
        assertEquals(productId, productDTO.getId());
        assertEquals("Test Product", productDTO.getName());
        assertEquals("TEST-001", productDTO.getSku());
        verify(productMapper).selectById(productId);
    }

    @Test
    void testGetProductById_NotFound() {
        // 准备测试数据
        Long productId = 1L;

        // 配置mock行为
        when(productMapper.selectById(productId)).thenReturn(null);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.getProductById(productId);
        });

        // 验证结果
        assertEquals(404, exception.getCode());
        assertEquals("商品不存在", exception.getMessage());
        verify(productMapper).selectById(productId);
    }

    @Test
    void testGetProductBySku_Success() {
        // 准备测试数据
        String sku = "TEST-001";
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setSku(sku);
        product.setPrice(100.0);

        // 配置mock行为
        when(productMapper.selectBySku(sku)).thenReturn(product);

        // 执行被测方法
        ProductDTO productDTO = productService.getProductBySku(sku);

        // 验证结果
        assertNotNull(productDTO);
        assertEquals(1L, productDTO.getId());
        assertEquals("Test Product", productDTO.getName());
        assertEquals(sku, productDTO.getSku());
        verify(productMapper).selectBySku(sku);
    }

    @Test
    void testGetProductList_Success() {
        // 准备测试数据
        ProductQueryRequest queryRequest = new ProductQueryRequest();
        queryRequest.setPage(1);
        queryRequest.setSize(10);
        queryRequest.setName("Test");

        int offset = (queryRequest.getPage() - 1) * queryRequest.getSize();

        List<Product> productList = new ArrayList<>();
        Product product1 = new Product();
        product1.setId(1L);
        product1.setName("Test Product 1");
        Product product2 = new Product();
        product2.setId(2L);
        product2.setName("Test Product 2");
        productList.add(product1);
        productList.add(product2);

        // 配置mock行为
        when(productMapper.selectAll(offset, queryRequest.getSize(), "Test", null, null, null, null, null))
                .thenReturn(productList);

        // 执行被测方法
        List<ProductDTO> productDTOList = productService.getProductList(queryRequest);

        // 验证结果
        assertNotNull(productDTOList);
        assertEquals(2, productDTOList.size());
        verify(productMapper).selectAll(offset, queryRequest.getSize(), "Test", null, null, null, null, null);
    }

    @Test
    void testGetProductCount_Success() {
        // 准备测试数据
        ProductQueryRequest queryRequest = new ProductQueryRequest();
        queryRequest.setName("Test");

        // 配置mock行为
        when(productMapper.count("Test", null, null, null)).thenReturn(10);

        // 执行被测方法
        int count = productService.getProductCount(queryRequest);

        // 验证结果
        assertEquals(10, count);
        verify(productMapper).count("Test", null, null, null);
    }

    @Test
    void testUpdateProduct_Success() {
        // 准备测试数据
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(150.0);

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Product");
        existingProduct.setSku("TEST-001");
        existingProduct.setPrice(100.0);

        // 配置mock行为
        when(productMapper.selectById(productId)).thenReturn(existingProduct);
        when(productMapper.update(any(Product.class))).thenReturn(1);

        // 执行被测方法
        ProductDTO updatedProductDTO = productService.updateProduct(productId, productDTO);

        // 验证结果
        assertNotNull(updatedProductDTO);
        verify(productMapper).selectById(productId);
        verify(productMapper).update(any(Product.class));
    }

    @Test
    void testDeleteProduct_Success() {
        // 准备测试数据
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");

        // 配置mock行为
        when(productMapper.selectById(productId)).thenReturn(product);
        when(productMapper.deleteById(productId)).thenReturn(1);

        // 执行被测方法
        productService.deleteProduct(productId);

        // 验证结果
        verify(productMapper).selectById(productId);
        verify(productMapper).deleteById(productId);
        verify(productInventoryMapper).deleteByProductId(productId);
    }

    @Test
    void testEnableProduct_Success() {
        // 准备测试数据
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setStatus(ProductStatus.INACTIVE);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Test Product");
        updatedProduct.setStatus(ProductStatus.ACTIVE);

        // 配置mock行为
        when(productMapper.selectById(productId)).thenReturn(product);
        when(productMapper.update(any(Product.class))).thenReturn(1);

        // 执行被测方法
        ProductDTO productDTO = productService.enableProduct(productId);

        // 验证结果
        assertNotNull(productDTO);
        assertEquals(ProductStatus.ACTIVE.getValue(), productDTO.getStatus());
        verify(productMapper).selectById(productId);
        verify(productMapper).update(any(Product.class));
    }

    @Test
    void testDisableProduct_Success() {
        // 准备测试数据
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setStatus(ProductStatus.ACTIVE);

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setName("Test Product");
        updatedProduct.setStatus(ProductStatus.INACTIVE);

        // 配置mock行为
        when(productMapper.selectById(productId)).thenReturn(product);
        when(productMapper.update(any(Product.class))).thenReturn(1);

        // 执行被测方法
        ProductDTO productDTO = productService.disableProduct(productId);

        // 验证结果
        assertNotNull(productDTO);
        assertEquals(ProductStatus.INACTIVE.getValue(), productDTO.getStatus());
        verify(productMapper).selectById(productId);
        verify(productMapper).update(any(Product.class));
    }

    @Test
    void testGetProductInventory_Success() {
        // 准备测试数据
        Long productId = 1L;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);
        inventory.setReservedQuantity(10);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);

        // 执行被测方法
        ProductInventory result = productService.getProductInventory(productId);

        // 验证结果
        assertNotNull(result);
        assertEquals(100, result.getQuantity());
        assertEquals(10, result.getReservedQuantity());
        verify(productInventoryMapper).selectByProductId(productId);
    }

    @Test
    void testUpdateProductInventory_Success() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 200;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);

        ProductInventory updatedInventory = new ProductInventory();
        updatedInventory.setProductId(productId);
        updatedInventory.setQuantity(quantity);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);
        when(productInventoryMapper.update(any(ProductInventory.class))).thenReturn(1);

        // 执行被测方法
        ProductInventory result = productService.updateProductInventory(productId, quantity);

        // 验证结果
        assertNotNull(result);
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper).update(any(ProductInventory.class));
    }

    @Test
    void testIncreaseProductInventory_Success() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 50;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);

        ProductInventory updatedInventory = new ProductInventory();
        updatedInventory.setProductId(productId);
        updatedInventory.setQuantity(150);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);
        when(productInventoryMapper.increaseQuantity(productId, quantity)).thenReturn(1);
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(updatedInventory);

        // 执行被测方法
        ProductInventory result = productService.increaseProductInventory(productId, quantity);

        // 验证结果
        assertNotNull(result);
        assertEquals(150, result.getQuantity());
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper).increaseQuantity(productId, quantity);
    }

    @Test
    void testDecreaseProductInventory_Success() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 30;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);

        ProductInventory updatedInventory = new ProductInventory();
        updatedInventory.setProductId(productId);
        updatedInventory.setQuantity(70);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);
        when(productInventoryMapper.decreaseQuantity(productId, quantity)).thenReturn(1);
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(updatedInventory);

        // 执行被测方法
        ProductInventory result = productService.decreaseProductInventory(productId, quantity);

        // 验证结果
        assertNotNull(result);
        assertEquals(70, result.getQuantity());
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper).decreaseQuantity(productId, quantity);
    }

    @Test
    void testDecreaseProductInventory_InsufficientStock() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 150;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);
        when(productInventoryMapper.decreaseQuantity(productId, quantity)).thenReturn(0);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productService.decreaseProductInventory(productId, quantity);
        });

        // 验证结果
        assertEquals(400, exception.getCode());
        assertEquals("库存不足", exception.getMessage());
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper).decreaseQuantity(productId, quantity);
    }

    @Test
    void testCheckInventoryStock_Sufficient() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 50;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);

        // 执行被测方法
        boolean result = productService.checkInventoryStock(productId, quantity);

        // 验证结果
        assertTrue(result);
        verify(productInventoryMapper).selectByProductId(productId);
    }

    @Test
    void testCheckInventoryStock_Insufficient() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 150;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);

        // 执行被测方法
        boolean result = productService.checkInventoryStock(productId, quantity);

        // 验证结果
        assertFalse(result);
        verify(productInventoryMapper).selectByProductId(productId);
    }

    @Test
    void testReserveInventory_Success() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 30;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);
        inventory.setReservedQuantity(10);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);
        when(productInventoryMapper.update(any(ProductInventory.class))).thenReturn(1);

        // 执行被测方法
        boolean result = productService.reserveInventory(productId, quantity);

        // 验证结果
        assertTrue(result);
        assertEquals(40, inventory.getReservedQuantity());
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper).update(any(ProductInventory.class));
    }

    @Test
    void testReserveInventory_InsufficientStock() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 150;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setQuantity(100);
        inventory.setReservedQuantity(10);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);

        // 执行被测方法
        boolean result = productService.reserveInventory(productId, quantity);

        // 验证结果
        assertFalse(result);
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper, never()).update(any(ProductInventory.class));
    }

    @Test
    void testReleaseInventory_Success() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 20;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setReservedQuantity(50);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);
        when(productInventoryMapper.update(any(ProductInventory.class))).thenReturn(1);

        // 执行被测方法
        productService.releaseInventory(productId, quantity);

        // 验证结果
        assertEquals(30, inventory.getReservedQuantity());
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper).update(any(ProductInventory.class));
    }

    @Test
    void testReleaseInventory_ZeroQuantity() {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 100;
        ProductInventory inventory = new ProductInventory();
        inventory.setProductId(productId);
        inventory.setReservedQuantity(50);

        // 配置mock行为
        when(productInventoryMapper.selectByProductId(productId)).thenReturn(inventory);
        when(productInventoryMapper.update(any(ProductInventory.class))).thenReturn(1);

        // 执行被测方法
        productService.releaseInventory(productId, quantity);

        // 验证结果
        assertEquals(0, inventory.getReservedQuantity());
        verify(productInventoryMapper).selectByProductId(productId);
        verify(productInventoryMapper).update(any(ProductInventory.class));
    }
}