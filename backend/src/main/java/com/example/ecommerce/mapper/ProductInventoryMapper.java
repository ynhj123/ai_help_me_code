package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.ProductInventory;
import org.apache.ibatis.annotations.*;

/**
 * 商品库存Mapper接口
 */
@Mapper
public interface ProductInventoryMapper {

    /**
     * 插入商品库存
     *
     * @param inventory 商品库存
     * @return 影响行数
     */
    int insert(ProductInventory inventory);

    /**
     * 根据商品ID查询库存
     *
     * @param productId 商品ID
     * @return 商品库存
     */
    ProductInventory selectByProductId(Long productId);

    /**
     * 更新库存
     *
     * @param inventory 商品库存
     * @return 影响行数
     */
    int update(ProductInventory inventory);

    /**
     * 更新库存数量
     *
     * @param productId 商品ID
     * @param quantity 库存数量
     * @return 影响行数
     */
    int updateQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 增加库存
     *
     * @param productId 商品ID
     * @param quantity 增加数量
     * @return 影响行数
     */
    int increaseQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 减少库存
     *
     * @param productId 商品ID
     * @param quantity 减少数量
     * @return 影响行数
     */
    int decreaseQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 更新预留库存
     *
     * @param productId 商品ID
     * @param reservedQuantity 预留库存数量
     * @return 影响行数
     */
    int updateReservedQuantity(@Param("productId") Long productId, @Param("reservedQuantity") Integer reservedQuantity);

    /**
     * 根据商品ID删除库存
     *
     * @param productId 商品ID
     * @return 影响行数
     */
    int deleteByProductId(Long productId);
}