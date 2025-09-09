package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.dto.ProductQueryRequest;
import com.example.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ProductController的测试类
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCreateProduct() throws Exception {
        // 准备测试数据
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Test Product");
        productDTO.setSku("TEST-001");
        productDTO.setPrice(new BigDecimal(100.0));
        productDTO.setDescription("Test Description");

        ProductDTO createdProductDTO = new ProductDTO();
        createdProductDTO.setId(1L);
        createdProductDTO.setName("Test Product");
        createdProductDTO.setSku("TEST-001");
        createdProductDTO.setPrice(new BigDecimal(100.0));
        createdProductDTO.setDescription("Test Description");

        // 配置mock行为
        when(productService.createProduct(any(ProductDTO.class))).thenReturn(createdProductDTO);

        // 执行被测方法
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.sku").value("TEST-001"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(header().string("Location", "/api/products/1"));

        // 验证结果
        verify(productService).createProduct(any(ProductDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateProduct_Forbidden() throws Exception {
        // 准备测试数据
        ProductDTO productDTO = new ProductDTO();

        // 执行被测方法并验证权限
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isForbidden());

        // 验证结果
        verify(productService, never()).createProduct(any(ProductDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateProduct() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Updated Product");
        productDTO.setPrice(new BigDecimal(150.0));

        ProductDTO updatedProductDTO = new ProductDTO();
        updatedProductDTO.setId(productId);
        updatedProductDTO.setName("Updated Product");
        updatedProductDTO.setPrice(new BigDecimal(150.0));

        // 配置mock行为
        when(productService.updateProduct(eq(productId), any(ProductDTO.class))).thenReturn(updatedProductDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(150.0));

        // 验证结果
        verify(productService).updateProduct(eq(productId), any(ProductDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetProductById() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setName("Test Product");
        productDTO.setSku("TEST-001");
        productDTO.setPrice(new BigDecimal(100.0));

        // 配置mock行为
        when(productService.getProductById(productId)).thenReturn(productDTO);

        // 执行被测方法
        mockMvc.perform(get("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.sku").value("TEST-001"))
                .andExpect(jsonPath("$.price").value(100.0));

        // 验证结果
        verify(productService).getProductById(productId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetProductBySku() throws Exception {
        // 准备测试数据
        String sku = "TEST-001";
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("Test Product");
        productDTO.setSku(sku);
        productDTO.setPrice(new BigDecimal(100.0));

        // 配置mock行为
        when(productService.getProductBySku(sku)).thenReturn(productDTO);

        // 执行被测方法
        mockMvc.perform(get("/api/products/sku/{sku}", sku)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.sku").value(sku))
                .andExpect(jsonPath("$.price").value(100.0));

        // 验证结果
        verify(productService).getProductBySku(sku);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetProductList() throws Exception {
        // 准备测试数据
        ProductQueryRequest queryRequest = new ProductQueryRequest();
        queryRequest.setPage(1);
        queryRequest.setSize(10);
        queryRequest.setName("Test");

        List<ProductDTO> productList = new ArrayList<>();
        ProductDTO product1 = new ProductDTO();
        product1.setId(1L);
        product1.setName("Test Product 1");
        ProductDTO product2 = new ProductDTO();
        product2.setId(2L);
        product2.setName("Test Product 2");
        productList.add(product1);
        productList.add(product2);

        // 配置mock行为
        when(productService.getProductList(any(ProductQueryRequest.class))).thenReturn(productList);

        // 执行被测方法
        mockMvc.perform(get("/api/products")
                .param("page", "1")
                .param("size", "10")
                .param("name", "Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Product 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Test Product 2"));

        // 验证结果
        verify(productService).getProductList(any(ProductQueryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetProductCount() throws Exception {
        // 准备测试数据
        ProductQueryRequest queryRequest = new ProductQueryRequest();
        queryRequest.setName("Test");

        // 配置mock行为
        when(productService.getProductCount(any(ProductQueryRequest.class))).thenReturn(5);

        // 执行被测方法
        mockMvc.perform(get("/api/products/count")
                .param("name", "Test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        // 验证结果
        verify(productService).getProductCount(any(ProductQueryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteProduct() throws Exception {
        // 准备测试数据
        Long productId = 1L;

        // 执行被测方法
        mockMvc.perform(delete("/api/products/{id}", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 验证结果
        verify(productService).deleteProduct(productId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testEnableProduct() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setName("Test Product");
        productDTO.setStatus(1); // 1表示启用

        // 配置mock行为
        when(productService.enableProduct(productId)).thenReturn(productDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/products/{id}/enable", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.status").value(1));

        // 验证结果
        verify(productService).enableProduct(productId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDisableProduct() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(productId);
        productDTO.setName("Test Product");
        productDTO.setStatus(0); // 0表示禁用

        // 配置mock行为
        when(productService.disableProduct(productId)).thenReturn(productDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/products/{id}/disable", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.status").value(0));

        // 验证结果
        verify(productService).disableProduct(productId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetProductInventory() throws Exception {
        // 准备测试数据
        Long productId = 1L;

        // 配置mock行为
        when(productService.getProductInventory(productId)).thenReturn(100);

        // 执行被测方法
        mockMvc.perform(get("/api/products/{id}/inventory", productId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        // 验证结果
        verify(productService).getProductInventory(productId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateProductInventory() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 200;

        // 配置mock行为
        when(productService.updateProductInventory(productId, quantity)).thenReturn(quantity);

        // 执行被测方法
        mockMvc.perform(put("/api/products/{id}/inventory", productId)
                .param("quantity", String.valueOf(quantity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(quantity)));

        // 验证结果
        verify(productService).updateProductInventory(productId, quantity);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testCheckInventory() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 50;

        // 配置mock行为
        when(productService.checkInventoryStock(productId, quantity)).thenReturn(true);

        // 执行被测方法
        mockMvc.perform(get("/api/products/{id}/inventory/check", productId)
                .param("quantity", String.valueOf(quantity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 验证结果
        verify(productService).checkInventoryStock(productId, quantity);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testReserveInventory() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 30;

        // 配置mock行为
        when(productService.reserveInventory(productId, quantity)).thenReturn(true);

        // 执行被测方法
        mockMvc.perform(post("/api/products/{id}/inventory/reserve", productId)
                .param("quantity", String.valueOf(quantity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        // 验证结果
        verify(productService).reserveInventory(productId, quantity);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testReleaseInventory() throws Exception {
        // 准备测试数据
        Long productId = 1L;
        Integer quantity = 20;

        // 执行被测方法
        mockMvc.perform(post("/api/products/{id}/inventory/release", productId)
                .param("quantity", String.valueOf(quantity))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 验证结果
        verify(productService).releaseInventory(productId, quantity);
    }
}