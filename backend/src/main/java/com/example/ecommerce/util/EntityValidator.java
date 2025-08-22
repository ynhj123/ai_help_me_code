package com.example.ecommerce.util;

import com.example.ecommerce.exception.BusinessException;
import org.springframework.stereotype.Component;

/**
 * 实体验证工具类
 * 统一处理实体存在性验证，减少重复代码
 */
@Component
public class EntityValidator {

    /**
     * 验证实体是否存在，如果不存在则抛出异常
     *
     * @param entity     实体对象
     * @param entityName 实体名称（用于错误消息）
     * @throws BusinessException 当实体不存在时抛出
     */
    public void validateEntityExists(Object entity, String entityName) {
        if (entity == null) {
            throw new BusinessException(404, entityName + "不存在");
        }
    }

    /**
     * 验证实体不为null，如果为null则抛出异常
     *
     * @param entity     实体对象
     * @param entityName 实体名称（用于错误消息）
     * @throws BusinessException 当实体为null时抛出
     */
    public void validateEntityNotNull(Object entity, String entityName) {
        if (entity == null) {
            throw new BusinessException(400, entityName + "不能为空");
        }
    }

    /**
     * 验证条件是否为真，如果为假则抛出异常
     *
     * @param condition 验证条件
     * @param message   错误消息
     * @throws BusinessException 当条件为假时抛出
     */
    public void validateCondition(boolean condition, String message) {
        if (!condition) {
            throw new BusinessException(400, message);
        }
    }

    /**
     * 验证实体唯一性，如果不唯一则抛出异常
     *
     * @param exists  是否存在标识
     * @param message 错误消息
     * @throws BusinessException 当实体已存在时抛出
     */
    public void validateEntityUnique(boolean exists, String message) {
        if (exists) {
            throw new BusinessException(409, message);
        }
    }

    /**
     * 验证字符串不为空
     *
     * @param value     字符串值
     * @param fieldName 字段名称
     * @throws BusinessException 当字符串为空时抛出
     */
    public void validateStringNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException(400, fieldName + "不能为空");
        }
    }

    /**
     * 验证数值为正数
     *
     * @param value     数值
     * @param fieldName 字段名称
     * @throws BusinessException 当数值不为正数时抛出
     */
    public void validatePositiveNumber(Number value, String fieldName) {
        if (value == null || value.doubleValue() <= 0) {
            throw new BusinessException(400, fieldName + "必须为正数");
        }
    }

    /**
     * 验证数值不为负数
     *
     * @param value     数值
     * @param fieldName 字段名称
     * @throws BusinessException 当数值为负数时抛出
     */
    public void validateNonNegativeNumber(Number value, String fieldName) {
        if (value == null || value.doubleValue() < 0) {
            throw new BusinessException(400, fieldName + "不能为负数");
        }
    }

    /**
     * 验证ID有效性
     *
     * @param id        ID值
     * @param fieldName 字段名称
     * @throws BusinessException 当ID无效时抛出
     */
    public void validateValidId(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new BusinessException(400, fieldName + "无效");
        }
    }
}