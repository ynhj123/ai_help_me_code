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
    @Insert("INSERT INTO product_inventory (product_id, quantity, reserved_quantity, created_at, updated_at) " +
            "VALUES (#{productId}, #{quantity}, #{reservedQuantity}, #{createdAt}, #{updatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ProductInventory inventory);

    /**
     * 根据商品ID查询库存
     *
     * @param productId 商品ID
     * @return 商品库存
     */
    @Select("SELECT * FROM product_inventory WHERE product_id = #{productId}")
    ProductInventory selectByProductId(Long productId);

    /**
     * 更新库存
     *
     * @param inventory 商品库存
     * @return 影响行数
     */
    @Update("UPDATE product_inventory SET quantity = #{quantity}, reserved_quantity = #{reservedQuantity}, updated_at = #{updatedAt} " +
            "WHERE product_id = #{productId}")
    int update(ProductInventory inventory);

    /**
     * 更新库存数量
     *
     * @param productId 商品ID
     * @param quantity 库存数量
     * @return 影响行数
     */
    @Update("UPDATE product_inventory SET quantity = #{quantity}, updated_at = NOW() WHERE product_id = #{productId}")
    int updateQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 增加库存
     *
     * @param productId 商品ID
     * @param quantity 增加数量
     * @return 影响行数
     */
    @Update("UPDATE product_inventory SET quantity = quantity + #{quantity}, updated_at = NOW() WHERE product_id = #{productId}")
    int increaseQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 减少库存
     *
     * @param productId 商品ID
     * @param quantity 减少数量
     * @return 影响行数
     */
    @Update("UPDATE product_inventory SET quantity = quantity - #{quantity}, updated_at = NOW() WHERE product_id = #{productId} AND quantity >= #{quantity}")
    int decreaseQuantity(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 更新预留库存
     *
     * @param productId 商品ID
     * @param reservedQuantity 预留库存数量
     * @return 影响行数
     */
    @Update("UPDATE product_inventory SET reserved_quantity = #{reservedQuantity}, updated_at = NOW() WHERE product_id = #{productId}")
    int updateReservedQuantity(@Param("productId") Long productId, @Param("reservedQuantity") Integer reservedQuantity);

    /**
     * 根据商品ID删除库存
     *
     * @param productId 商品ID
     * @return 影响行数
     */
    @Delete("DELETE FROM product_inventory WHERE product_id = #{productId}")
    int deleteByProductId(Long productId);
}