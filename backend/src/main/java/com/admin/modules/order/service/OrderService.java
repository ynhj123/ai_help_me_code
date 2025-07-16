package com.admin.modules.order.service;

import com.admin.modules.order.dto.OrderCreateRequest;
import com.admin.modules.order.dto.OrderDto;
import com.admin.modules.order.entity.Order;
import com.admin.modules.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    
    /**
     * 创建订单
     */
    OrderDto createOrder(OrderCreateRequest request, Long userId);
    
    /**
     * 根据订单号获取订单详情
     */
    OrderDto getOrderByNumber(String orderNumber);
    
    /**
     * 根据ID获取订单详情
     */
    OrderDto getOrderById(Long orderId);
    
    /**
     * 获取用户订单列表
     */
    Page<OrderDto> getUserOrders(Long userId, Pageable pageable);
    
    /**
     * 获取所有订单列表（管理员）
     */
    Page<OrderDto> getAllOrders(Pageable pageable);
    
    /**
     * 更新订单状态
     */
    OrderDto updateOrderStatus(Long orderId, OrderStatus status, String remark);
    
    /**
     * 取消订单
     */
    OrderDto cancelOrder(Long orderId, String reason);
    
    /**
     * 删除订单（软删除）
     */
    void deleteOrder(Long orderId);
    
    /**
     * 根据订单号获取订单实体
     */
    Order getOrderEntityByNumber(String orderNumber);
}