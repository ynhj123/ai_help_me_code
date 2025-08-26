package com.example.ecommerce.service.impl;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.dto.OrderQueryRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.OrderItemMapper;
import com.example.ecommerce.mapper.OrderMapper;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // 生成订单编号
        String orderNo = generateOrderNo();
        orderDTO.setOrderNo(orderNo);

        // 计算订单总金额
        BigDecimal totalAmount = calculateOrderAmount(orderDTO.getOrderItems(),
                orderDTO.getShippingFee(), orderDTO.getDiscountAmount());
        orderDTO.setTotalAmount(totalAmount);
        orderDTO.setProductAmount(orderDTO.getOrderItems().stream()
                .map(OrderItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        orderDTO.setPaidAmount(totalAmount);

        // 设置订单状态为待付款
        orderDTO.setStatus(OrderStatus.PENDING_PAYMENT);
        orderDTO.setCreatedAt(LocalDateTime.now());
        orderDTO.setUpdatedAt(LocalDateTime.now());

        // 插入订单
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        orderMapper.insert(order);

        // 插入订单商品
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            OrderItem item = new OrderItem();
            BeanUtils.copyProperties(itemDTO, item);
            item.setOrderId(order.getId());
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            orderItems.add(item);
        }
        orderItemMapper.batchInsert(orderItems);

        // 转换为DTO
        OrderDTO createdOrderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, createdOrderDTO);
        createdOrderDTO.setOrderItems(orderDTO.getOrderItems());
        return createdOrderDTO;
    }

    @Override
    public OrderDTO getOrderById(Long id) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 查询订单商品
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(id);
        List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            return itemDTO;
        }).collect(Collectors.toList());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        orderDTO.setOrderItems(orderItemDTOs);
        return orderDTO;
    }

    @Override
    public OrderDTO getOrderByOrderNo(String orderNo) {
        // 查询订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 查询订单商品
        List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
        List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(item -> {
            OrderItemDTO itemDTO = new OrderItemDTO();
            BeanUtils.copyProperties(item, itemDTO);
            return itemDTO;
        }).collect(Collectors.toList());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        orderDTO.setOrderItems(orderItemDTOs);
        return orderDTO;
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        // 查询订单列表
        List<Order> orderList = orderMapper.selectByUserId(userId);

        // 转换为DTO列表
        return orderList.stream().map(order -> {
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
            List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                BeanUtils.copyProperties(item, itemDTO);
                return itemDTO;
            }).collect(Collectors.toList());

            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(order, orderDTO);
            orderDTO.setOrderItems(orderItemDTOs);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrderList(Integer page, Integer size) {
        // 计算偏移量
        int offset = (page - 1) * size;

        // 查询订单列表
        List<Order> orderList = orderMapper.selectPage(offset, size);

        // 转换为DTO列表
        return orderList.stream().map(order -> {
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
            List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                BeanUtils.copyProperties(item, itemDTO);
                return itemDTO;
            }).collect(Collectors.toList());

            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(order, orderDTO);
            orderDTO.setOrderItems(orderItemDTOs);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrderList(OrderQueryRequest queryRequest) {
        // 计算偏移量
        int offset = (queryRequest.getPage() - 1) * queryRequest.getSize();

        // 查询订单列表
        List<Order> orderList = orderMapper.selectAll(
                offset,
                queryRequest.getSize(),
                queryRequest.getOrderNo(),
                queryRequest.getUserId(),
                queryRequest.getStatus(),
                queryRequest.getPaymentMethod(),
                queryRequest.getStartTime(),
                queryRequest.getEndTime(),
                queryRequest.getSortBy(),
                queryRequest.getSortOrder()
        );

        // 转换为DTO列表
        return orderList.stream().map(order -> {
            List<OrderItem> orderItems = orderItemMapper.selectByOrderId(order.getId());
            List<OrderItemDTO> orderItemDTOs = orderItems.stream().map(item -> {
                OrderItemDTO itemDTO = new OrderItemDTO();
                BeanUtils.copyProperties(item, itemDTO);
                return itemDTO;
            }).collect(Collectors.toList());

            OrderDTO orderDTO = new OrderDTO();
            BeanUtils.copyProperties(order, orderDTO);
            orderDTO.setOrderItems(orderItemDTOs);
            return orderDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public int getOrderCount(OrderQueryRequest queryRequest) {
        return orderMapper.count(
                queryRequest.getOrderNo(),
                queryRequest.getUserId(),
                queryRequest.getStatus(),
                queryRequest.getPaymentMethod(),
                queryRequest.getStartTime(),
                queryRequest.getEndTime()
        );
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 更新订单
        BeanUtils.copyProperties(orderDTO, order, "id", "orderNo", "userId", "status",
                "createdAt", "paidAt", "shippedAt", "completedAt", "cancelledAt");
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.update(order);

        // 转换为DTO
        OrderDTO updatedOrderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, updatedOrderDTO);
        return updatedOrderDTO;
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 删除订单商品
        orderItemMapper.deleteByOrderId(id);

        // 删除订单
        orderMapper.deleteById(id);
    }

    @Override
    @Transactional
    public OrderDTO cancelOrder(Long id, String cancelReason) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 检查订单状态是否可以取消
        if (!canBeCancelled(order.getStatus().getCode())) {
            throw new BusinessException(400, "订单状态不允许取消");
        }

        // 更新订单状态
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancelReason(cancelReason);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.update(order);

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO payOrder(Long id) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 检查订单状态是否可以支付
        if (!canBePaid(order.getStatus().getCode())) {
            throw new BusinessException(400, "订单状态不允许支付");
        }

        // 更新订单状态
        LocalDateTime paidAt = LocalDateTime.now();
        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(paidAt);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updatePaymentStatus(id, paidAt, OrderStatus.PAID.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO shipOrder(Long id, String logisticsCompany, String trackingNumber) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 检查订单状态是否可以发货
        if (!canBeShipped(order.getStatus().getCode())) {
            throw new BusinessException(400, "订单状态不允许发货");
        }

        // 更新订单状态
        LocalDateTime shippedAt = LocalDateTime.now();
        order.setStatus(OrderStatus.SHIPPED);
        order.setLogisticsCompany(logisticsCompany);
        order.setTrackingNumber(trackingNumber);
        order.setShippedAt(shippedAt);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateShippingStatus(id, logisticsCompany, trackingNumber, shippedAt, OrderStatus.SHIPPED.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO confirmOrder(Long id) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 检查订单状态是否可以确认收货
        if (!canBeConfirmed(order.getStatus().getCode())) {
            throw new BusinessException(400, "订单状态不允许确认收货");
        }

        // 更新订单状态
        LocalDateTime completedAt = LocalDateTime.now();
        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(completedAt);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateCompletedStatus(id, completedAt, OrderStatus.COMPLETED.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 更新订单状态
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateStatus(id, status.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO updatePaymentStatus(Long id, LocalDateTime paidAt, OrderStatus status) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 更新支付状态
        order.setPaidAt(paidAt);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updatePaymentStatus(id, paidAt, status.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO updateShippingStatus(Long id, String logisticsCompany, String trackingNumber,
                                         LocalDateTime shippedAt, OrderStatus status) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 更新发货状态
        order.setLogisticsCompany(logisticsCompany);
        order.setTrackingNumber(trackingNumber);
        order.setShippedAt(shippedAt);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateShippingStatus(id, logisticsCompany, trackingNumber, shippedAt, status.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO updateCompletedStatus(Long id, LocalDateTime completedAt, OrderStatus status) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 更新完成状态
        order.setCompletedAt(completedAt);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateCompletedStatus(id, completedAt, status.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO updateCancelledStatus(Long id, LocalDateTime cancelledAt, String cancelReason, OrderStatus status) {
        // 查询订单
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(404, "订单不存在");
        }

        // 更新取消状态
        order.setCancelledAt(cancelledAt);
        order.setCancelReason(cancelReason);
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateCancelledStatus(id, cancelledAt, cancelReason, status.getCode());

        // 转换为DTO
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(order, orderDTO);
        return orderDTO;
    }

    @Override
    public String generateOrderNo() {
        // 生成订单编号：日期 + 时间 + 随机数
        String date = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDateTime.now());
        String time = java.time.format.DateTimeFormatter.ofPattern("HHmmss").format(LocalDateTime.now());
        String random = String.format("%04d", (int) (Math.random() * 10000));
        return "ORD" + date + time + random;
    }

    @Override
    public BigDecimal calculateOrderAmount(List<OrderItemDTO> orderItems, BigDecimal shippingFee, BigDecimal discountAmount) {
        BigDecimal productAmount = orderItems.stream()
                .map(OrderItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = productAmount.add(shippingFee).subtract(discountAmount);
        return totalAmount.compareTo(BigDecimal.ZERO) > 0 ? totalAmount : BigDecimal.ZERO;
    }

    @Override
    public boolean existsById(Long id) {
        return orderMapper.selectById(id) != null;
    }

    @Override
    public boolean existsByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo) != null;
    }

    @Override
    public boolean belongsToUser(Long id, Long userId) {
        Order order = orderMapper.selectById(id);
        return order != null && order.getUserId().equals(userId);
    }

    @Override
    public boolean canBeCancelled(Integer status) {
        return OrderStatus.PENDING_PAYMENT.getCode() == (status);
    }

    @Override
    public boolean canBePaid(Integer status) {
        return OrderStatus.PENDING_PAYMENT.getCode() == (status);
    }

    @Override
    public boolean canBeShipped(Integer status) {
        return OrderStatus.PAID.getCode() == (status);
    }

    @Override
    public boolean canBeConfirmed(Integer status) {
        return OrderStatus.SHIPPED.getCode() == (status);
    }
}