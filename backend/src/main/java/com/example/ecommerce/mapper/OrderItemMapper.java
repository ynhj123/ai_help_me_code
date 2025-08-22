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
    int insert(OrderItem orderItem);

    /**
     * 批量插入订单商品
     *
     * @param orderItems 订单商品列表
     * @return 影响行数
     */
    int batchInsert(@Param("orderItems") List<OrderItem> orderItems);

    /**
     * 根据订单ID查询订单商品列表
     *
     * @param orderId 订单ID
     * @return 订单商品列表
     */
    List<OrderItem> selectByOrderId(Long orderId);

    /**
     * 根据ID查询订单商品
     *
     * @param id 订单商品ID
     * @return 订单商品
     */
    OrderItem selectById(Long id);

    /**
     * 根据订单ID删除订单商品
     *
     * @param orderId 订单ID
     * @return 影响行数
     */
    int deleteByOrderId(Long orderId);

    /**
     * 根据ID删除订单商品
     *
     * @param id 订单商品ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 更新订单商品
     *
     * @param orderItem 订单商品
     * @return 影响行数
     */
    int update(OrderItem orderItem);

    /**
     * 根据订单ID更新订单商品信息
     *
     * @param orderId 订单ID
     * @param orderItems 订单商品列表
     * @return 影响行数
     */
    int batchUpdate(@Param("orderId") Long orderId, @Param("orderItems") List<OrderItem> orderItems);
}