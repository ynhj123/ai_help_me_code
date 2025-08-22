package com.example.ecommerce.util;

import com.example.ecommerce.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EntityValidator单元测试
 */
@ExtendWith(MockitoExtension.class)
class EntityValidatorTest {

    private EntityValidator entityValidator;

    @BeforeEach
    void setUp() {
        entityValidator = new EntityValidator();
    }

    @Test
    void testValidateEntityExists_WhenEntityIsNull_ThrowsBusinessException() {
        // Given
        Object entity = null;
        String entityName = "测试实体";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> entityValidator.validateEntityExists(entity, entityName));
        
        assertEquals(404, exception.getCode());
        assertEquals("测试实体不存在", exception.getMessage());
    }

    @Test
    void testValidateEntityExists_WhenEntityExists_NoException() {
        // Given
        Object entity = new Object();
        String entityName = "测试实体";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateEntityExists(entity, entityName));
    }

    @Test
    void testValidateEntityNotNull_WhenEntityIsNull_ThrowsBusinessException() {
        // Given
        Object entity = null;
        String entityName = "测试实体";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateEntityNotNull(entity, entityName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试实体不能为空", exception.getMessage());
    }

    @Test
    void testValidateEntityNotNull_WhenEntityExists_NoException() {
        // Given
        Object entity = new Object();
        String entityName = "测试实体";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateEntityNotNull(entity, entityName));
    }

    @Test
    void testValidateCondition_WhenConditionIsFalse_ThrowsBusinessException() {
        // Given
        boolean condition = false;
        String message = "条件验证失败";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateCondition(condition, message));
        
        assertEquals(400, exception.getCode());
        assertEquals("条件验证失败", exception.getMessage());
    }

    @Test
    void testValidateCondition_WhenConditionIsTrue_NoException() {
        // Given
        boolean condition = true;
        String message = "条件验证失败";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateCondition(condition, message));
    }

    @Test
    void testValidateEntityUnique_WhenEntityExists_ThrowsBusinessException() {
        // Given
        boolean exists = true;
        String message = "实体已存在";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateEntityUnique(exists, message));
        
        assertEquals(409, exception.getCode());
        assertEquals("实体已存在", exception.getMessage());
    }

    @Test
    void testValidateEntityUnique_WhenEntityNotExists_NoException() {
        // Given
        boolean exists = false;
        String message = "实体已存在";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateEntityUnique(exists, message));
    }

    @Test
    void testValidateStringNotEmpty_WhenStringIsNull_ThrowsBusinessException() {
        // Given
        String value = null;
        String fieldName = "测试字段";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateStringNotEmpty(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试字段不能为空", exception.getMessage());
    }

    @Test
    void testValidateStringNotEmpty_WhenStringIsEmpty_ThrowsBusinessException() {
        // Given
        String value = "";
        String fieldName = "测试字段";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateStringNotEmpty(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试字段不能为空", exception.getMessage());
    }

    @Test
    void testValidateStringNotEmpty_WhenStringIsBlank_ThrowsBusinessException() {
        // Given
        String value = "   ";
        String fieldName = "测试字段";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateStringNotEmpty(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试字段不能为空", exception.getMessage());
    }

    @Test
    void testValidateStringNotEmpty_WhenStringHasValue_NoException() {
        // Given
        String value = "有效值";
        String fieldName = "测试字段";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateStringNotEmpty(value, fieldName));
    }

    @Test
    void testValidatePositiveNumber_WhenNumberIsNull_ThrowsBusinessException() {
        // Given
        Number value = null;
        String fieldName = "测试数值";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validatePositiveNumber(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试数值必须为正数", exception.getMessage());
    }

    @Test
    void testValidatePositiveNumber_WhenNumberIsZero_ThrowsBusinessException() {
        // Given
        Number value = 0;
        String fieldName = "测试数值";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validatePositiveNumber(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试数值必须为正数", exception.getMessage());
    }

    @Test
    void testValidatePositiveNumber_WhenNumberIsNegative_ThrowsBusinessException() {
        // Given
        Number value = -1;
        String fieldName = "测试数值";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validatePositiveNumber(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试数值必须为正数", exception.getMessage());
    }

    @Test
    void testValidatePositiveNumber_WhenNumberIsPositive_NoException() {
        // Given
        Number value = 10;
        String fieldName = "测试数值";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validatePositiveNumber(value, fieldName));
    }

    @Test
    void testValidateNonNegativeNumber_WhenNumberIsNull_ThrowsBusinessException() {
        // Given
        Number value = null;
        String fieldName = "测试数值";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateNonNegativeNumber(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试数值不能为负数", exception.getMessage());
    }

    @Test
    void testValidateNonNegativeNumber_WhenNumberIsNegative_ThrowsBusinessException() {
        // Given
        Number value = -1;
        String fieldName = "测试数值";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateNonNegativeNumber(value, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试数值不能为负数", exception.getMessage());
    }

    @Test
    void testValidateNonNegativeNumber_WhenNumberIsZero_NoException() {
        // Given
        Number value = 0;
        String fieldName = "测试数值";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateNonNegativeNumber(value, fieldName));
    }

    @Test
    void testValidateNonNegativeNumber_WhenNumberIsPositive_NoException() {
        // Given
        Number value = 10;
        String fieldName = "测试数值";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateNonNegativeNumber(value, fieldName));
    }

    @Test
    void testValidateValidId_WhenIdIsNull_ThrowsBusinessException() {
        // Given
        Long id = null;
        String fieldName = "测试ID";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateValidId(id, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试ID无效", exception.getMessage());
    }

    @Test
    void testValidateValidId_WhenIdIsZero_ThrowsBusinessException() {
        // Given
        Long id = 0L;
        String fieldName = "测试ID";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateValidId(id, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试ID无效", exception.getMessage());
    }

    @Test
    void testValidateValidId_WhenIdIsNegative_ThrowsBusinessException() {
        // Given
        Long id = -1L;
        String fieldName = "测试ID";

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class,
            () -> entityValidator.validateValidId(id, fieldName));
        
        assertEquals(400, exception.getCode());
        assertEquals("测试ID无效", exception.getMessage());
    }

    @Test
    void testValidateValidId_WhenIdIsValid_NoException() {
        // Given
        Long id = 1L;
        String fieldName = "测试ID";

        // When & Then
        assertDoesNotThrow(() -> entityValidator.validateValidId(id, fieldName));
    }
}