package com.example.ecommerce.util;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO转换工具类
 * 统一处理实体与DTO之间的转换，减少重复代码
 */
@Component
public class DTOConverter {

    /**
     * 将实体对象转换为DTO对象
     *
     * @param entity   实体对象
     * @param dtoClass DTO类型
     * @param <T>      DTO类型泛型
     * @return DTO对象
     */
    public <T> T toDTO(Object entity, Class<T> dtoClass) {
        if (entity == null) {
            return null;
        }
        
        try {
            T dto = dtoClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("实体转换DTO失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将DTO对象转换为实体对象
     *
     * @param dto         DTO对象
     * @param entityClass 实体类型
     * @param <T>         实体类型泛型
     * @return 实体对象
     */
    public <T> T toEntity(Object dto, Class<T> entityClass) {
        if (dto == null) {
            return null;
        }
        
        try {
            T entity = entityClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(dto, entity);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("DTO转换实体失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将实体列表转换为DTO列表
     *
     * @param entityList 实体列表
     * @param dtoClass   DTO类型
     * @param <T>        DTO类型泛型
     * @return DTO列表
     */
    public <T> List<T> toDTOList(List<?> entityList, Class<T> dtoClass) {
        if (entityList == null || entityList.isEmpty()) {
            return List.of();
        }
        
        return entityList.stream()
                .map(entity -> toDTO(entity, dtoClass))
                .collect(Collectors.toList());
    }

    /**
     * 将DTO列表转换为实体列表
     *
     * @param dtoList     DTO列表
     * @param entityClass 实体类型
     * @param <T>         实体类型泛型
     * @return 实体列表
     */
    public <T> List<T> toEntityList(List<?> dtoList, Class<T> entityClass) {
        if (dtoList == null || dtoList.isEmpty()) {
            return List.of();
        }
        
        return dtoList.stream()
                .map(dto -> toEntity(dto, entityClass))
                .collect(Collectors.toList());
    }

    /**
     * 复制属性（排除指定字段）
     *
     * @param source       源对象
     * @param target       目标对象
     * @param excludeFields 排除的字段名数组
     */
    public void copyPropertiesWithExclusions(Object source, Object target, String... excludeFields) {
        if (source == null || target == null) {
            return;
        }
        
        try {
            BeanUtils.copyProperties(source, target, excludeFields);
        } catch (Exception e) {
            throw new RuntimeException("属性复制失败: " + e.getMessage(), e);
        }
    }

    /**
     * 更新实体对象（只复制非空字段）
     *
     * @param dto    DTO对象
     * @param entity 实体对象
     */
    public void updateEntityFromDTO(Object dto, Object entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        try {
            // 使用Spring的BeanUtils复制非空属性
            BeanUtils.copyProperties(dto, entity, getNullPropertyNames(dto));
        } catch (Exception e) {
            throw new RuntimeException("实体更新失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取对象中为null的属性名数组
     *
     * @param source 源对象
     * @return null属性名数组
     */
    private String[] getNullPropertyNames(Object source) {
        if (source == null) {
            return new String[0];
        }
        
        try {
            java.beans.BeanInfo beanInfo = java.beans.Introspector.getBeanInfo(source.getClass());
            java.beans.PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            
            return java.util.Arrays.stream(propertyDescriptors)
                    .filter(pd -> {
                        try {
                            if (pd.getReadMethod() == null) {
                                return false;
                            }
                            Object value = pd.getReadMethod().invoke(source);
                            return value == null;
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .map(java.beans.PropertyDescriptor::getName)
                    .filter(name -> !"class".equals(name))
                    .toArray(String[]::new);
        } catch (Exception e) {
            throw new RuntimeException("获取null属性失败: " + e.getMessage(), e);
        }
    }

    /**
     * 合并两个对象的属性（目标对象为null的字段从源对象复制）
     *
     * @param source 源对象
     * @param target 目标对象
     */
    public void mergeProperties(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        
        try {
            BeanUtils.copyProperties(source, target, getNullPropertyNames(target));
        } catch (Exception e) {
            throw new RuntimeException("属性合并失败: " + e.getMessage(), e);
        }
    }
}