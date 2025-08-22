package com.example.ecommerce.util;

import com.example.ecommerce.dto.ProductDTO;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.enums.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * DTOConverter单元测试
 */
@ExtendWith(MockitoExtension.class)
class DTOConverterTest {

    private DTOConverter dtoConverter;

    @BeforeEach
    void setUp() {
        dtoConverter = new DTOConverter();
    }

    @Test
    void testToDTO_WhenEntityIsNull_ReturnsNull() {
        // Given
        Product entity = null;

        // When
        ProductDTO result = dtoConverter.toDTO(entity, ProductDTO.class);

        // Then
        assertNull(result);
    }

    @Test
    void testToDTO_WhenEntityIsValid_ReturnsDTO() {
        // Given
        Product entity = createTestProduct();

        // When
        ProductDTO result = dtoConverter.toDTO(entity, ProductDTO.class);

        // Then
        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getCategoryId(), result.getCategoryId());
        assertEquals(entity.getPrice(), result.getPrice());
        assertEquals(entity.getMarketPrice(), result.getMarketPrice());
        assertEquals(entity.getCostPrice(), result.getCostPrice());
        assertEquals(entity.getSku(), result.getSku());
        assertEquals(entity.getBarcode(), result.getBarcode());
        assertEquals(entity.getImage(), result.getImage());
        assertEquals(entity.getGallery(), result.getGallery());
        assertEquals(entity.getDetail(), result.getDetail());
        assertEquals(entity.getStatus(), result.getStatus());
        assertEquals(entity.getCreatedAt(), result.getCreatedAt());
        assertEquals(entity.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void testToEntity_WhenDTOIsNull_ReturnsNull() {
        // Given
        ProductDTO dto = null;

        // When
        Product result = dtoConverter.toEntity(dto, Product.class);

        // Then
        assertNull(result);
    }

    @Test
    void testToEntity_WhenDTOIsValid_ReturnsEntity() {
        // Given
        ProductDTO dto = createTestProductDTO();

        // When
        Product result = dtoConverter.toEntity(dto, Product.class);

        // Then
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getCategoryId(), result.getCategoryId());
        assertEquals(dto.getPrice(), result.getPrice());
        assertEquals(dto.getMarketPrice(), result.getMarketPrice());
        assertEquals(dto.getCostPrice(), result.getCostPrice());
        assertEquals(dto.getSku(), result.getSku());
        assertEquals(dto.getBarcode(), result.getBarcode());
        assertEquals(dto.getImage(), result.getImage());
        assertEquals(dto.getGallery(), result.getGallery());
        assertEquals(dto.getDetail(), result.getDetail());
        assertEquals(dto.getStatus(), result.getStatus());
        assertEquals(dto.getCreatedAt(), result.getCreatedAt());
        assertEquals(dto.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    void testToDTOList_WhenListIsNull_ReturnsNull() {
        // Given
        List<Product> entityList = null;

        // When
        List<ProductDTO> result = dtoConverter.toDTOList(entityList, ProductDTO.class);

        // Then
        assertNull(result);
    }

    @Test
    void testToDTOList_WhenListIsEmpty_ReturnsEmptyList() {
        // Given
        List<Product> entityList = Arrays.asList();

        // When
        List<ProductDTO> result = dtoConverter.toDTOList(entityList, ProductDTO.class);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToDTOList_WhenListHasEntities_ReturnsDTOList() {
        // Given
        Product entity1 = createTestProduct();
        entity1.setId(1L);
        entity1.setName("商品1");

        Product entity2 = createTestProduct();
        entity2.setId(2L);
        entity2.setName("商品2");

        List<Product> entityList = Arrays.asList(entity1, entity2);

        // When
        List<ProductDTO> result = dtoConverter.toDTOList(entityList, ProductDTO.class);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("商品1", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("商品2", result.get(1).getName());
    }

    @Test
    void testToEntityList_WhenListIsNull_ReturnsNull() {
        // Given
        List<ProductDTO> dtoList = null;

        // When
        List<Product> result = dtoConverter.toEntityList(dtoList, Product.class);

        // Then
        assertNull(result);
    }

    @Test
    void testToEntityList_WhenListIsEmpty_ReturnsEmptyList() {
        // Given
        List<ProductDTO> dtoList = Arrays.asList();

        // When
        List<Product> result = dtoConverter.toEntityList(dtoList, Product.class);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToEntityList_WhenListHasDTOs_ReturnsEntityList() {
        // Given
        ProductDTO dto1 = createTestProductDTO();
        dto1.setId(1L);
        dto1.setName("商品1");

        ProductDTO dto2 = createTestProductDTO();
        dto2.setId(2L);
        dto2.setName("商品2");

        List<ProductDTO> dtoList = Arrays.asList(dto1, dto2);

        // When
        List<Product> result = dtoConverter.toEntityList(dtoList, Product.class);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("商品1", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("商品2", result.get(1).getName());
    }

    @Test
    void testUpdateEntityFromDTO_WhenDTOIsNull_NoException() {
        // Given
        ProductDTO dto = null;
        Product entity = createTestProduct();
        String originalName = entity.getName();

        // When & Then
        assertDoesNotThrow(() -> dtoConverter.updateEntityFromDTO(dto, entity));
        assertEquals(originalName, entity.getName()); // 确保实体没有被修改
    }

    @Test
    void testUpdateEntityFromDTO_WhenEntityIsNull_NoException() {
        // Given
        ProductDTO dto = createTestProductDTO();
        Product entity = null;

        // When & Then
        assertDoesNotThrow(() -> dtoConverter.updateEntityFromDTO(dto, entity));
    }

    @Test
    void testUpdateEntityFromDTO_WhenBothAreValid_UpdatesEntity() {
        // Given
        Product entity = createTestProduct();
        entity.setName("原始名称");
        entity.setDescription("原始描述");

        ProductDTO dto = new ProductDTO();
        dto.setName("更新后的名称");
        dto.setDescription("更新后的描述");
        dto.setPrice(new BigDecimal("99.99"));

        // When
        dtoConverter.updateEntityFromDTO(dto, entity);

        // Then
        assertEquals("更新后的名称", entity.getName());
        assertEquals("更新后的描述", entity.getDescription());
        assertEquals(new BigDecimal("99.99"), entity.getPrice());
    }

    private Product createTestProduct() {
        Product product = new Product();
        product.setId(1L);
        product.setName("测试商品");
        product.setDescription("测试商品描述");
        product.setCategoryId(1L);
        product.setPrice(new BigDecimal("100.00"));
        product.setMarketPrice(new BigDecimal("120.00"));
        product.setCostPrice(new BigDecimal("80.00"));
        product.setSku("TEST-SKU-001");
        product.setBarcode("1234567890123");
        product.setImage("/images/test.jpg");
        product.setGallery("/images/gallery/");
        product.setDetail("详细商品信息");
        product.setStatus(ProductStatus.ACTIVE);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        return product;
    }

    private ProductDTO createTestProductDTO() {
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("测试商品");
        dto.setDescription("测试商品描述");
        dto.setCategoryId(1L);
        dto.setPrice(new BigDecimal("100.00"));
        dto.setMarketPrice(new BigDecimal("120.00"));
        dto.setCostPrice(new BigDecimal("80.00"));
        dto.setSku("TEST-SKU-001");
        dto.setBarcode("1234567890123");
        dto.setImage("/images/test.jpg");
        dto.setGallery("/images/gallery/");
        dto.setDetail("详细商品信息");
        dto.setStatus(ProductStatus.ACTIVE);
        dto.setCreatedAt(LocalDateTime.now());
        dto.setUpdatedAt(LocalDateTime.now());
        return dto;
    }
}