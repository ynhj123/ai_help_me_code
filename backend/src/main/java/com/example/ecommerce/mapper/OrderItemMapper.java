package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单商品Mapper接口
 */
@Mapper
public interface OrderItemMapper {

    /**
     * 插入订单商品
     *
     * @param orderItem 订单商品
     * @return 影响行数
     */
    @Insert({
        "<script>",
        "INSERT INTO order_items (order_id, product_id, product_name, product_image, product_sku, " +
        "product_price, quantity, subtotal, category_id, category_name, created_at, updated_at)",
        "VALUES (#{orderId}, #{productId}, #{productName}, #{productImage}, #{productSku}, " +
        "#{productPrice}, #{quantity}, #{subtotal}, #{categoryId}, #{categoryName}, #{createdAt}, #{updatedAt})",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(OrderItem orderItem);

    /**
     * 批量插入订单商品
     *
     * @param orderItems 订单商品列表
     * @return 影响行数
     */
    @Insert({
        "<script>",
        "INSERT INTO order_items (order_id, product_id, product_name, product_image, product_sku, " +
        "product_price, quantity, subtotal, category_id, category_name, created_at, updated_at)",
        "VALUES ",
        "<foreach collection='orderItems' item='item' separator=','>",
        "(#{item.orderId}, #{item.productId}, #{item.productName}, #{item.productImage}, #{item.productSku}, " +
        "#{item.productPrice}, #{item.quantity}, #{item.subtotal}, #{item.categoryId}, #{item.categoryName}, " +
        "#{item.createdAt}, #{item.updatedAt})",
        "</foreach>",
        "</script>"
    })
    int batchInsert(@Param("orderItems") List<OrderItem> orderItems);

    /**
     * 根据订单ID查询订单商品列表
     *
     * @param orderId 订单ID
     * @return 订单商品列表
     */
    @Select("SELECT * FROM order_items WHERE order_id = #{orderId} ORDER BY id ASC")
    List<OrderItem> selectByOrderId(Long orderId);

    /**
     * 根据ID查询订单商品
     *
     * @param id 订单商品ID
     * @return 订单商品
     */
    @Select("SELECT * FROM order_items WHERE id = #{id}")
    OrderItem selectById(Long id);

    /**
     * 根据订单ID删除订单商品
     *
     * @param orderId 订单ID
     * @return 影响行数
     */
    @Delete("DELETE FROM order_items WHERE order_id = #{orderId}")
    int deleteByOrderId(Long orderId);

    /**
     * 根据ID删除订单商品
     *
     * @param id 订单商品ID
     * @return 影响行数
     */
    @Delete("DELETE FROM order_items WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 更新订单商品
     *
     * @param orderItem 订单商品
     * @return 影响行数
     */
    @Update({
        "<script>",
        "UPDATE order_items",
        "<set>",
        "<if test='productName != null'>product_name = #{productName},</if>",
        "<if test='productImage != null'>product_image = #{productImage},</if>",
        "<if test='productSku != null'>product_sku = #{productSku},</if>",
        "<if test='productPrice != null'>product_price = #{productPrice},</if>",
        "<if test='quantity != null'>quantity = #{quantity},</if>",
        "<if test='subtotal != null'>subtotal = #{subtotal},</if>",
        "<if test='categoryId != null'>category_id = #{categoryId},</if>",
        "<if test='categoryName != null'>category_name = #{categoryName},</if>",
        "updated_at = #{updatedAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(OrderItem orderItem);

    /**
     * 根据订单ID更新订单商品信息
     *
     * @param orderId 订单ID
     * @param orderItems 订单商品列表
     * @return 影响行数
     */
    @Update({
        "<script>",
        "<foreach collection='orderItems' item='item' separator=';'>",
        "UPDATE order_items SET product_name = #{item.productName}, product_image = #{item.productImage}, " +
        "product_sku = #{item.productSku}, product_price = #{item.productPrice}, quantity = #{item.quantity}, " +
        "subtotal = #{item.subtotal}, category_id = #{item.categoryId}, category_name = #{item.categoryName}, " +
        "updated_at = #{item.updatedAt} WHERE id = #{item.id}",
        "</foreach>",
        "</script>"
    })
    int batchUpdate(@Param("orderId") Long orderId, @Param("orderItems") List<OrderItem> orderItems);
}