package com.admin.modules.order.service;

import com.admin.common.exception.ResourceNotFoundException;
import com.admin.modules.order.dto.OrderCreateRequest;
import com.admin.modules.order.dto.OrderDto;
import com.admin.modules.order.dto.OrderItemCreateRequest;
import com.admin.modules.order.entity.*;
import com.admin.modules.order.enums.OrderStatus;
import com.admin.modules.order.enums.PaymentStatus;
import com.admin.modules.order.mapper.OrderMapper;
import com.admin.modules.order.repository.OrderRepository;
import com.admin.modules.product.entity.Sku;
import com.admin.modules.product.repository.SkuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final SkuRepository skuRepository;
    private final OrderMapper orderMapper;
    
    @Override
    public OrderDto createOrder(OrderCreateRequest request, Long userId) {
        // 生成订单号
        String orderNumber = generateOrderNumber();
        
        // 创建订单
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.UNPAID);
        
        // 设置收货地址
        OrderAddress address = new OrderAddress();
        address.setOrder(order);
        address.setReceiverName(request.getAddress().getReceiverName());
        address.setReceiverPhone(request.getAddress().getReceiverPhone());
        address.setProvince(request.getAddress().getProvince());
        address.setCity(request.getAddress().getCity());
        address.setDistrict(request.getAddress().getDistrict());
        address.setAddress(request.getAddress().getAddress());
        address.setPostalCode(request.getAddress().getPostalCode());
        address.setRemark(request.getAddress().getRemark());
        order.setAddress(address);
        
        // 设置支付方式
        order.setPaymentMethod(request.getPaymentMethod());
        order.setRemark(request.getRemark());
        
        // 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;
        
        // 创建订单项
        for (OrderItemCreateRequest itemRequest : request.getItems()) {
            Sku sku = skuRepository.findById(itemRequest.getSkuId())
                    .orElseThrow(() -> new ResourceNotFoundException("SKU不存在"));
            
            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProductId(itemRequest.getProductId());
            item.setSkuId(itemRequest.getSkuId());
            item.setProductName(sku.getProduct().getName());
            item.setProductImage(sku.getProduct().getImages().isEmpty() ? 
                    null : sku.getProduct().getImages().get(0).getUrl());
            item.setSkuName(sku.getName());
            item.setPrice(sku.getPrice());
            item.setOriginalPrice(sku.getPrice());
            item.setQuantity(itemRequest.getQuantity());
            item.setTotalAmount(sku.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            item.setSpecifications(sku.getSpecifications());
            
            order.getItems().add(item);
            totalAmount = totalAmount.add(item.getTotalAmount());
        }
        
        order.setTotalAmount(totalAmount);
        order.setPayableAmount(totalAmount.subtract(discountAmount));
        order.setDiscountAmount(discountAmount);
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumberAndIsDeletedFalse(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在"));
        return orderMapper.toDto(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderById(Long orderId) {
        Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在"));
        return orderMapper.toDto(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getUserOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(userId, pageable)
                .map(orderMapper::toDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getAllOrders(Pageable pageable) {
        return orderRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable)
                .map(orderMapper::toDto);
    }
    
    @Override
    public OrderDto updateOrderStatus(Long orderId, OrderStatus status, String remark) {
        Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在"));
        
        order.setStatus(status);
        
        // 设置对应的时间
        LocalDateTime now = LocalDateTime.now();
        switch (status) {
            case PAID:
                order.setPaidAt(now);
                order.setPaymentStatus(PaymentStatus.PAID);
                break;
            case SHIPPED:
                order.setShippedAt(now);
                break;
            case DELIVERED:
                order.setDeliveredAt(now);
                break;
            case COMPLETED:
                order.setCompletedAt(now);
                break;
            case CANCELLED:
                order.setCancelledAt(now);
                break;
        }
        
        if (remark != null) {
            order.setRemark(remark);
        }
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }
    
    @Override
    public OrderDto cancelOrder(Long orderId, String reason) {
        Order order = orderRepository.findByIdAndIsDeletedFalse(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在"));
        
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("只有待付款的订单才能取消");
        }
        
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancelReason(reason);
        
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }
    
    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在"));
        
        order.setIsDeleted(true);
        orderRepository.save(order);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Order getOrderEntityByNumber(String orderNumber) {
        return orderRepository.findByOrderNumberAndIsDeletedFalse(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在"));
    }
    
    private String generateOrderNumber() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}