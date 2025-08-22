package com.example.ecommerce.service;

import com.example.ecommerce.util.DTOConverter;
import com.example.ecommerce.util.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 基础Service抽象类
 * 提供通用的CRUD操作模板方法，集成EntityValidator和DTOConverter
 *
 * @param <T> 实体类型
 * @param <D> DTO类型
 */
public abstract class BaseService<T, D> {

    @Autowired
    protected EntityValidator entityValidator;

    @Autowired
    protected DTOConverter dtoConverter;

    /**
     * 获取Mapper接口
     * 子类必须实现此方法提供具体的Mapper
     *
     * @return Mapper接口
     */
    protected abstract Object getMapper();

    /**
     * 获取实体类型
     * 子类必须实现此方法提供实体Class
     *
     * @return 实体Class
     */
    protected abstract Class<T> getEntityClass();

    /**
     * 获取DTO类型
     * 子类必须实现此方法提供DTO Class
     *
     * @return DTO Class
     */
    protected abstract Class<D> getDTOClass();

    /**
     * 获取实体名称（用于错误消息）
     * 子类可以重写此方法提供自定义的实体名称
     *
     * @return 实体名称
     */
    protected String getEntityName() {
        return getEntityClass().getSimpleName();
    }

    /**
     * 创建实体
     * 通用的创建流程：DTO转实体 -> 设置创建时间 -> 执行创建 -> 转换返回DTO
     *
     * @param dto DTO对象
     * @return 创建后的DTO对象
     */
    public D create(D dto) {
        // 验证DTO不为空
        entityValidator.validateEntityNotNull(dto, getEntityName() + "信息");
        
        // DTO转实体
        T entity = dtoConverter.toEntity(dto, getEntityClass());
        
        // 设置创建时间和更新时间
        setCreateTime(entity);
        setUpdateTime(entity);
        
        // 执行创建前的业务验证
        validateBeforeCreate(entity);
        
        // 执行创建
        doCreate(entity);
        
        // 转换返回DTO
        return dtoConverter.toDTO(entity, getDTOClass());
    }

    /**
     * 根据ID获取实体
     *
     * @param id 实体ID
     * @return DTO对象
     */
    public D getById(Long id) {
        // 验证ID有效性
        entityValidator.validateValidId(id, getEntityName() + "ID");
        
        // 查询实体
        T entity = doGetById(id);
        
        // 验证实体存在
        entityValidator.validateEntityExists(entity, getEntityName());
        
        // 转换返回DTO
        return dtoConverter.toDTO(entity, getDTOClass());
    }

    /**
     * 更新实体
     *
     * @param id  实体ID
     * @param dto DTO对象
     * @return 更新后的DTO对象
     */
    public D update(Long id, D dto) {
        // 验证参数
        entityValidator.validateValidId(id, getEntityName() + "ID");
        entityValidator.validateEntityNotNull(dto, getEntityName() + "信息");
        
        // 查询现有实体
        T existingEntity = doGetById(id);
        entityValidator.validateEntityExists(existingEntity, getEntityName());
        
        // 执行更新前的业务验证
        validateBeforeUpdate(id, dto, existingEntity);
        
        // 更新实体属性
        dtoConverter.updateEntityFromDTO(dto, existingEntity);
        
        // 设置更新时间
        setUpdateTime(existingEntity);
        
        // 执行更新
        doUpdate(existingEntity);
        
        // 转换返回DTO
        return dtoConverter.toDTO(existingEntity, getDTOClass());
    }

    /**
     * 删除实体
     *
     * @param id 实体ID
     */
    public void delete(Long id) {
        // 验证ID有效性
        entityValidator.validateValidId(id, getEntityName() + "ID");
        
        // 查询实体是否存在
        T entity = doGetById(id);
        entityValidator.validateEntityExists(entity, getEntityName());
        
        // 执行删除前的业务验证
        validateBeforeDelete(id, entity);
        
        // 执行删除
        doDelete(id);
    }

    /**
     * 分页查询
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return DTO列表
     */
    public List<D> list(int offset, int limit) {
        // 验证分页参数
        entityValidator.validateNonNegativeNumber(offset, "偏移量");
        entityValidator.validatePositiveNumber(limit, "限制数量");
        
        // 查询实体列表
        List<T> entityList = doList(offset, limit);
        
        // 转换返回DTO列表
        return dtoConverter.toDTOList(entityList, getDTOClass());
    }

    // ================ 抽象方法，子类必须实现 ================

    /**
     * 执行创建操作
     * 子类实现具体的创建逻辑
     *
     * @param entity 实体对象
     */
    protected abstract void doCreate(T entity);

    /**
     * 根据ID查询实体
     * 子类实现具体的查询逻辑
     *
     * @param id 实体ID
     * @return 实体对象
     */
    protected abstract T doGetById(Long id);

    /**
     * 执行更新操作
     * 子类实现具体的更新逻辑
     *
     * @param entity 实体对象
     */
    protected abstract void doUpdate(T entity);

    /**
     * 执行删除操作
     * 子类实现具体的删除逻辑
     *
     * @param id 实体ID
     */
    protected abstract void doDelete(Long id);

    /**
     * 分页查询实体列表
     * 子类实现具体的查询逻辑
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 实体列表
     */
    protected abstract List<T> doList(int offset, int limit);

    // ================ 钩子方法，子类可以重写 ================

    /**
     * 创建前的业务验证
     * 子类可以重写此方法添加特定的验证逻辑
     *
     * @param entity 实体对象
     */
    protected void validateBeforeCreate(T entity) {
        // 默认无额外验证
    }

    /**
     * 更新前的业务验证
     * 子类可以重写此方法添加特定的验证逻辑
     *
     * @param id             实体ID
     * @param dto            DTO对象
     * @param existingEntity 现有实体
     */
    protected void validateBeforeUpdate(Long id, D dto, T existingEntity) {
        // 默认无额外验证
    }

    /**
     * 删除前的业务验证
     * 子类可以重写此方法添加特定的验证逻辑
     *
     * @param id     实体ID
     * @param entity 实体对象
     */
    protected void validateBeforeDelete(Long id, T entity) {
        // 默认无额外验证
    }

    // ================ 工具方法 ================

    /**
     * 设置创建时间
     * 如果实体有createdAt字段，则设置当前时间
     *
     * @param entity 实体对象
     */
    private void setCreateTime(T entity) {
        try {
            var method = entity.getClass().getMethod("setCreatedAt", LocalDateTime.class);
            method.invoke(entity, LocalDateTime.now());
        } catch (Exception e) {
            // 如果没有setCreatedAt方法，忽略
        }
    }

    /**
     * 设置更新时间
     * 如果实体有updatedAt字段，则设置当前时间
     *
     * @param entity 实体对象
     */
    private void setUpdateTime(T entity) {
        try {
            var method = entity.getClass().getMethod("setUpdatedAt", LocalDateTime.class);
            method.invoke(entity, LocalDateTime.now());
        } catch (Exception e) {
            // 如果没有setUpdatedAt方法，忽略
        }
    }
}