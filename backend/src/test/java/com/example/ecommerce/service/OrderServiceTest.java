package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.dto.OrderQueryRequest;
import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.OrderItem;
import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.mapper.OrderItemMapper;
import com.example.ecommerce.mapper.OrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * OrderService的测试类
 */
class OrderServiceTest {

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderItemMapper orderItemMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder_Success() {
        // 准备测试数据
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setTotalAmount(new BigDecimal(200.0));
        orderDTO.setShippingAddress("测试地址");
        orderDTO.setContactPhone("13800138000");

        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        OrderItemDTO itemDTO1 = new OrderItemDTO();
        itemDTO1.setProductId(1L);
        itemDTO1.setProductName("测试商品1");
        itemDTO1.setProductSku("TEST-001");
        itemDTO1.setQuantity(2);
        itemDTO1.setPrice(new BigDecimal(100.0));
        orderItemDTOs.add(itemDTO1);
        orderDTO.setOrderItems(orderItemDTOs);

        Order order = new Order();
        order.setId(1L);
        order.setOrderNo("ORD-20240101-00001");
        order.setUserId(1L);
        order.setTotalAmount(new BigDecimal(200.0));
        order.setShippingAddress("测试地址");
        order.setContactPhone("13800138000");
        order.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        // 配置mock行为
        when(orderMapper.insert(any(Order.class))).thenReturn(1);
        when(orderMapper.selectByOrderNo("ORD-20240101-00001")).thenReturn(order);
        when(productService.reserveInventory(anyLong(), anyInt())).thenReturn(true);
        when(orderItemMapper.insertBatch(anyList())).thenReturn(1);

        // 执行被测方法
        OrderDTO createdOrderDTO = orderService.createOrder(orderDTO);

        // 验证结果
        assertNotNull(createdOrderDTO);
        assertEquals(1L, createdOrderDTO.getId());
        assertEquals(OrderStatus.PENDING_PAYMENT.getValue(), createdOrderDTO.getStatus());
        verify(orderMapper).insert(any(Order.class));
        verify(productService).reserveInventory(1L, 2);
        verify(orderItemMapper).insertBatch(anyList());
    }

    @Test
    void testCreateOrder_ReserveInventoryFailed() {
        // 准备测试数据
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setTotalAmount(new BigDecimal(200.0));

        List<OrderItemDTO> orderItemDTOs = new ArrayList<>();
        OrderItemDTO itemDTO1 = new OrderItemDTO();
        itemDTO1.setProductId(1L);
        itemDTO1.setQuantity(2);
        orderItemDTOs.add(itemDTO1);
        orderDTO.setOrderItems(orderItemDTOs);

        // 配置mock行为
        when(productService.reserveInventory(anyLong(), anyInt())).thenReturn(false);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(orderDTO);
        });

        // 验证结果
        assertEquals(400, exception.getCode());
        assertEquals("商品1库存不足，无法创建订单", exception.getMessage());
        verify(productService).reserveInventory(1L, 2);
        verify(orderMapper, never()).insert(any(Order.class));
        verify(orderItemMapper, never()).insertBatch(anyList());
    }

    @Test
    void testGetOrderById_Success() {
        // 准备测试数据
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo("ORD-20240101-00001");
        order.setUserId(1L);
        order.setTotalAmount(new BigDecimal(200.0));
        order.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setOrderId(orderId);
        item.setProductId(1L);
        item.setQuantity(2);
        item.setPrice(new BigDecimal(100.0));
        orderItems.add(item);

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(orderId)).thenReturn(orderItems);

        // 执行被测方法
        OrderDTO orderDTO = orderService.getOrderById(orderId);

        // 验证结果
        assertNotNull(orderDTO);
        assertEquals(orderId, orderDTO.getId());
        assertEquals("ORD-20240101-00001", orderDTO.getOrderNo());
        assertEquals(1, orderDTO.getOrderItems().size());
        verify(orderMapper).selectById(orderId);
        verify(orderItemMapper).selectByOrderId(orderId);
    }

    @Test
    void testGetOrderById_NotFound() {
        // 准备测试数据
        Long orderId = 1L;

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(null);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.getOrderById(orderId);
        });

        // 验证结果
        assertEquals(404, exception.getCode());
        assertEquals("订单不存在", exception.getMessage());
        verify(orderMapper).selectById(orderId);
        verify(orderItemMapper, never()).selectByOrderId(anyLong());
    }

    @Test
    void testGetOrderByOrderNo_Success() {
        // 准备测试数据
        String orderNo = "ORD-20240101-00001";
        Order order = new Order();
        order.setId(1L);
        order.setOrderNo(orderNo);
        order.setUserId(1L);
        order.setTotalAmount(new BigDecimal(200.0));

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setOrderId(1L);
        item.setProductId(1L);
        orderItems.add(item);

        // 配置mock行为
        when(orderMapper.selectByOrderNo(orderNo)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(1L)).thenReturn(orderItems);

        // 执行被测方法
        OrderDTO orderDTO = orderService.getOrderByOrderNo(orderNo);

        // 验证结果
        assertNotNull(orderDTO);
        assertEquals(orderNo, orderDTO.getOrderNo());
        verify(orderMapper).selectByOrderNo(orderNo);
        verify(orderItemMapper).selectByOrderId(1L);
    }

    @Test
    void testGetOrdersByUserId_Success() {
        // 准备测试数据
        Long userId = 1L;
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setId(1L);
        order1.setUserId(userId);
        order1.setOrderNo("ORD-20240101-00001");
        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(userId);
        order2.setOrderNo("ORD-20240101-00002");
        orders.add(order1);
        orders.add(order2);

        // 配置mock行为
        when(orderMapper.selectByUserId(userId)).thenReturn(orders);
        when(orderItemMapper.selectByOrderId(1L)).thenReturn(new ArrayList<>());
        when(orderItemMapper.selectByOrderId(2L)).thenReturn(new ArrayList<>());

        // 执行被测方法
        List<OrderDTO> orderDTOs = orderService.getOrdersByUserId(userId);

        // 验证结果
        assertNotNull(orderDTOs);
        assertEquals(2, orderDTOs.size());
        verify(orderMapper).selectByUserId(userId);
        verify(orderItemMapper).selectByOrderId(1L);
        verify(orderItemMapper).selectByOrderId(2L);
    }

    @Test
    void testGetOrderList_Success() {
        // 准备测试数据
        OrderQueryRequest queryRequest = new OrderQueryRequest();
        queryRequest.setPage(1);
        queryRequest.setSize(10);
        queryRequest.setUserId(1L);
        queryRequest.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        int offset = (queryRequest.getPage() - 1) * queryRequest.getSize();

        List<Order> orders = new ArrayList<>();
        Order order = new Order();
        order.setId(1L);
        order.setUserId(1L);
        order.setOrderNo("ORD-20240101-00001");
        orders.add(order);

        // 配置mock行为
        when(orderMapper.selectAll(offset, queryRequest.getSize(), 1L, null, 
                OrderStatus.PENDING_PAYMENT.getValue(), null, null)).thenReturn(orders);
        when(orderItemMapper.selectByOrderId(1L)).thenReturn(new ArrayList<>());

        // 执行被测方法
        List<OrderDTO> orderDTOs = orderService.getOrderList(queryRequest);

        // 验证结果
        assertNotNull(orderDTOs);
        assertEquals(1, orderDTOs.size());
        verify(orderMapper).selectAll(offset, queryRequest.getSize(), 1L, null, 
                OrderStatus.PENDING_PAYMENT.getValue(), null, null);
    }

    @Test
    void testGetOrderCount_Success() {
        // 准备测试数据
        OrderQueryRequest queryRequest = new OrderQueryRequest();
        queryRequest.setUserId(1L);
        queryRequest.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        // 配置mock行为
        when(orderMapper.count(1L, null, OrderStatus.PENDING_PAYMENT.getValue(), null, null)).thenReturn(5);

        // 执行被测方法
        int count = orderService.getOrderCount(queryRequest);

        // 验证结果
        assertEquals(5, count);
        verify(orderMapper).count(1L, null, OrderStatus.PENDING_PAYMENT.getValue(), null, null);
    }

    @Test
    void testUpdateOrder_Success() {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setShippingAddress("更新后的地址");
        orderDTO.setContactPhone("13900139000");

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setOrderNo("ORD-20240101-00001");
        existingOrder.setUserId(1L);
        existingOrder.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(existingOrder);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        when(orderMapper.selectById(orderId)).thenReturn(existingOrder);
        when(orderItemMapper.selectByOrderId(orderId)).thenReturn(new ArrayList<>());

        // 执行被测方法
        OrderDTO updatedOrderDTO = orderService.updateOrder(orderId, orderDTO);

        // 验证结果
        assertNotNull(updatedOrderDTO);
        assertEquals("更新后的地址", updatedOrderDTO.getShippingAddress());
        assertEquals("13900139000", updatedOrderDTO.getContactPhone());
        verify(orderMapper).selectById(orderId);
        verify(orderMapper).update(any(Order.class));
    }

    @Test
    void testUpdateOrder_OrderNotPending() {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.PAID.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(existingOrder);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.updateOrder(orderId, orderDTO);
        });

        // 验证结果
        assertEquals(400, exception.getCode());
        assertEquals("只有待付款的订单可以修改", exception.getMessage());
        verify(orderMapper).selectById(orderId);
        verify(orderMapper, never()).update(any(Order.class));
    }

    @Test
    void testDeleteOrder_Success() {
        // 准备测试数据
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo("ORD-20240101-00001");
        order.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setOrderId(orderId);
        item.setProductId(1L);
        item.setQuantity(2);
        orderItems.add(item);

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(orderId)).thenReturn(orderItems);
        when(orderMapper.deleteById(orderId)).thenReturn(1);
        when(orderItemMapper.deleteByOrderId(orderId)).thenReturn(1);
        when(productService.releaseInventory(1L, 2)).thenReturn(true);

        // 执行被测方法
        orderService.deleteOrder(orderId);

        // 验证结果
        verify(orderMapper).selectById(orderId);
        verify(orderMapper).deleteById(orderId);
        verify(orderItemMapper).deleteByOrderId(orderId);
        verify(productService).releaseInventory(1L, 2);
    }

    @Test
    void testDeleteOrder_OrderNotPending() {
        // 准备测试数据
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PAID.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);

        // 执行被测方法并验证异常
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.deleteOrder(orderId);
        });

        // 验证结果
        assertEquals(400, exception.getCode());
        assertEquals("只有待付款的订单可以删除", exception.getMessage());
        verify(orderMapper).selectById(orderId);
        verify(orderMapper, never()).deleteById(orderId);
    }

    @Test
    void testCancelOrder_Success() {
        // 准备测试数据
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo("ORD-20240101-00001");
        order.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setOrderId(orderId);
        item.setProductId(1L);
        item.setQuantity(2);
        orderItems.add(item);

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(orderId)).thenReturn(orderItems);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        when(productService.releaseInventory(1L, 2)).thenReturn(true);

        // 执行被测方法
        OrderDTO orderDTO = orderService.cancelOrder(orderId);

        // 验证结果
        assertNotNull(orderDTO);
        assertEquals(OrderStatus.CANCELLED.getValue(), orderDTO.getStatus());
        verify(orderMapper).selectById(orderId);
        verify(orderMapper).update(any(Order.class));
        verify(productService).releaseInventory(1L, 2);
    }

    @Test
    void testPayOrder_Success() {
        // 准备测试数据
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo("ORD-20240101-00001");
        order.setStatus(OrderStatus.PENDING_PAYMENT.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(orderId)).thenReturn(new ArrayList<>());

        // 执行被测方法
        OrderDTO orderDTO = orderService.payOrder(orderId);

        // 验证结果
        assertNotNull(orderDTO);
        assertEquals(OrderStatus.PAID.getValue(), orderDTO.getStatus());
        verify(orderMapper).selectById(orderId);
        verify(orderMapper).update(any(Order.class));
    }

    @Test
    void testShipOrder_Success() {
        // 准备测试数据
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo("ORD-20240101-00001");
        order.setStatus(OrderStatus.PAID.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(orderId)).thenReturn(new ArrayList<>());

        // 执行被测方法
        OrderDTO orderDTO = orderService.shipOrder(orderId);

        // 验证结果
        assertNotNull(orderDTO);
        assertEquals(OrderStatus.SHIPPED.getValue(), orderDTO.getStatus());
        verify(orderMapper).selectById(orderId);
        verify(orderMapper).update(any(Order.class));
    }

    @Test
    void testConfirmOrder_Success() {
        // 准备测试数据
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setOrderNo("ORD-20240101-00001");
        order.setStatus(OrderStatus.SHIPPED.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderMapper.update(any(Order.class))).thenReturn(1);
        when(orderMapper.selectById(orderId)).thenReturn(order);
        when(orderItemMapper.selectByOrderId(orderId)).thenReturn(new ArrayList<>());

        // 执行被测方法
        OrderDTO orderDTO = orderService.confirmOrder(orderId);

        // 验证结果
        assertNotNull(orderDTO);
        assertEquals(OrderStatus.COMPLETED.getValue(), orderDTO.getStatus());
        verify(orderMapper).selectById(orderId);
        verify(orderMapper).update(any(Order.class));
    }

    @Test
    void testCalculateOrderTotalAmount_Success() {
        // 准备测试数据
        List<OrderItemDTO> orderItems = new ArrayList<>();
        OrderItemDTO item1 = new OrderItemDTO();
        item1.setPrice(new BigDecimal(100.0));
        item1.setQuantity(2);
        orderItems.add(item1);
        OrderItemDTO item2 = new OrderItemDTO();
        item2.setPrice(new BigDecimal(50.0));
        item2.setQuantity(3);
        orderItems.add(item2);

        // 执行被测方法
        BigDecimal totalAmount = orderService.calculateOrderTotalAmount(orderItems);

        // 验证结果
        assertEquals(new BigDecimal(350.0), totalAmount);
    }

    @Test
    void testIsOrderStatusValid_Success() {
        // 准备测试数据
        Long orderId = 1L;
        OrderStatus expectedStatus = OrderStatus.PENDING_PAYMENT;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(expectedStatus.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);

        // 执行被测方法
        boolean result = orderService.isOrderStatusValid(orderId, expectedStatus);

        // 验证结果
        assertTrue(result);
        verify(orderMapper).selectById(orderId);
    }

    @Test
    void testIsOrderStatusValid_InvalidStatus() {
        // 准备测试数据
        Long orderId = 1L;
        OrderStatus expectedStatus = OrderStatus.PENDING_PAYMENT;

        Order order = new Order();
        order.setId(orderId);
        order.setStatus(OrderStatus.PAID.getValue());

        // 配置mock行为
        when(orderMapper.selectById(orderId)).thenReturn(order);

        // 执行被测方法
        boolean result = orderService.isOrderStatusValid(orderId, expectedStatus);

        // 验证结果
        assertFalse(result);
        verify(orderMapper).selectById(orderId);
    }

    @Test
    void testGenerateOrderNo_Success() {
        // 执行被测方法
        String orderNo = orderService.generateOrderNo();

        // 验证结果
        assertNotNull(orderNo);
        assertTrue(orderNo.startsWith("ORD-"));
        assertEquals(20, orderNo.length());
    }
}