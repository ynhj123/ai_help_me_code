package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderQueryRequest;
import com.example.ecommerce.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OrderController的测试类
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCreateOrder() throws Exception {
        // 准备测试数据
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(1L);
        orderDTO.setTotalAmount(new BigDecimal(100.0));

        OrderDTO createdOrderDTO = new OrderDTO();
        createdOrderDTO.setId(1L);
        createdOrderDTO.setOrderNo("ORD-20240101-00001");
        createdOrderDTO.setUserId(1L);
        createdOrderDTO.setTotalAmount(new BigDecimal(100.0));

        // 配置mock行为
        when(orderService.createOrder(any(OrderDTO.class))).thenReturn(createdOrderDTO);

        // 执行被测方法
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.0))
                .andExpect(header().string("Location", "/api/orders/1"));

        // 验证结果
        verify(orderService).createOrder(any(OrderDTO.class));
    }

    @Test
    void testCreateOrder_Unauthenticated() throws Exception {
        // 准备测试数据
        OrderDTO orderDTO = new OrderDTO();

        // 执行被测方法并验证认证
        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isUnauthorized());

        // 验证结果
        verify(orderService, never()).createOrder(any(OrderDTO.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetOrderById() throws Exception {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setOrderNo("ORD-20240101-00001");
        orderDTO.setUserId(1L);
        orderDTO.setTotalAmount(new BigDecimal(100.0));

        // 配置mock行为
        when(orderService.getOrderById(orderId)).thenReturn(orderDTO);

        // 执行被测方法
        mockMvc.perform(get("/api/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.0));

        // 验证结果
        verify(orderService).getOrderById(orderId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetOrderByOrderNo() throws Exception {
        // 准备测试数据
        String orderNo = "ORD-20240101-00001";
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setOrderNo(orderNo);
        orderDTO.setUserId(1L);
        orderDTO.setTotalAmount(new BigDecimal(100.0));

        // 配置mock行为
        when(orderService.getOrderByOrderNo(orderNo)).thenReturn(orderDTO);

        // 执行被测方法
        mockMvc.perform(get("/api/orders/orderNo/{orderNo}", orderNo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderNo").value(orderNo))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(100.0));

        // 验证结果
        verify(orderService).getOrderByOrderNo(orderNo);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetOrdersByUserId() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        List<OrderDTO> orderList = new ArrayList<>();
        OrderDTO order1 = new OrderDTO();
        order1.setId(1L);
        order1.setOrderNo("ORD-20240101-00001");
        order1.setUserId(userId);
        OrderDTO order2 = new OrderDTO();
        order2.setId(2L);
        order2.setOrderNo("ORD-20240102-00002");
        order2.setUserId(userId);
        orderList.add(order1);
        orderList.add(order2);

        // 配置mock行为
        when(orderService.getOrdersByUserId(eq(userId), any())).thenReturn(orderList);

        // 执行被测方法
        mockMvc.perform(get("/api/orders/user/{userId}", userId)
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].orderNo").value("ORD-20240102-00002"));

        // 验证结果
        verify(orderService).getOrdersByUserId(eq(userId), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetOrderList() throws Exception {
        // 准备测试数据
        OrderQueryRequest queryRequest = new OrderQueryRequest();
        queryRequest.setPage(1);
        queryRequest.setSize(10);
        queryRequest.setOrderNo("ORD-");

        List<OrderDTO> orderList = new ArrayList<>();
        OrderDTO order1 = new OrderDTO();
        order1.setId(1L);
        order1.setOrderNo("ORD-20240101-00001");
        OrderDTO order2 = new OrderDTO();
        order2.setId(2L);
        order2.setOrderNo("ORD-20240102-00002");
        orderList.add(order1);
        orderList.add(order2);

        // 配置mock行为
        when(orderService.getOrderList(any(OrderQueryRequest.class))).thenReturn(orderList);

        // 执行被测方法
        mockMvc.perform(get("/api/orders")
                .param("page", "1")
                .param("size", "10")
                .param("orderNo", "ORD-")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].orderNo").value("ORD-20240102-00002"));

        // 验证结果
        verify(orderService).getOrderList(any(OrderQueryRequest.class));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetOrderList_Forbidden() throws Exception {
        // 执行被测方法并验证权限
        mockMvc.perform(get("/api/orders")
                .param("page", "1")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // 验证结果
        verify(orderService, never()).getOrderList(any(OrderQueryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testGetOrderCount() throws Exception {
        // 准备测试数据
        OrderQueryRequest queryRequest = new OrderQueryRequest();
        queryRequest.setOrderNo("ORD-");

        // 配置mock行为
        when(orderService.getOrderCount(any(OrderQueryRequest.class))).thenReturn(5);

        // 执行被测方法
        mockMvc.perform(get("/api/orders/count")
                .param("orderNo", "ORD-")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        // 验证结果
        verify(orderService).getOrderCount(any(OrderQueryRequest.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testUpdateOrder() throws Exception {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setStatus(2); // 2表示支付状态
        orderDTO.setPaymentAmount(new BigDecimal(100.0));

        OrderDTO updatedOrderDTO = new OrderDTO();
        updatedOrderDTO.setId(orderId);
        updatedOrderDTO.setOrderNo("ORD-20240101-00001");
        updatedOrderDTO.setStatus(2);
        updatedOrderDTO.setPaymentAmount(new BigDecimal(100.0));

        // 配置mock行为
        when(orderService.updateOrder(eq(orderId), any(OrderDTO.class))).thenReturn(updatedOrderDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$.status").value(2))
                .andExpect(jsonPath("$.paymentAmount").value(100.0));

        // 验证结果
        verify(orderService).updateOrder(eq(orderId), any(OrderDTO.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testDeleteOrder() throws Exception {
        // 准备测试数据
        Long orderId = 1L;

        // 执行被测方法
        mockMvc.perform(delete("/api/orders/{id}", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // 验证结果
        verify(orderService).deleteOrder(orderId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCancelOrder() throws Exception {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setOrderNo("ORD-20240101-00001");
        orderDTO.setStatus(3); // 3表示取消状态

        // 配置mock行为
        when(orderService.cancelOrder(orderId)).thenReturn(orderDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/orders/{id}/cancel", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$.status").value(3));

        // 验证结果
        verify(orderService).cancelOrder(orderId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testPayOrder() throws Exception {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setOrderNo("ORD-20240101-00001");
        orderDTO.setStatus(2); // 2表示支付状态
        orderDTO.setPaymentAmount(new BigDecimal(100.0));

        // 配置mock行为
        when(orderService.payOrder(orderId)).thenReturn(orderDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/orders/{id}/pay", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$.status").value(2))
                .andExpect(jsonPath("$.paymentAmount").value(100.0));

        // 验证结果
        verify(orderService).payOrder(orderId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void testShipOrder() throws Exception {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setOrderNo("ORD-20240101-00001");
        orderDTO.setStatus(4); // 4表示发货状态
        orderDTO.setShippingAddress("Test Address");
        orderDTO.setTrackingNumber("TRK-0001");

        // 配置mock行为
        when(orderService.shipOrder(orderId)).thenReturn(orderDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/orders/{id}/ship", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$.status").value(4))
                .andExpect(jsonPath("$.shippingAddress").value("Test Address"))
                .andExpect(jsonPath("$.trackingNumber").value("TRK-0001"));

        // 验证结果
        verify(orderService).shipOrder(orderId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testConfirmOrder() throws Exception {
        // 准备测试数据
        Long orderId = 1L;
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderId);
        orderDTO.setOrderNo("ORD-20240101-00001");
        orderDTO.setStatus(5); // 5表示确认收货状态

        // 配置mock行为
        when(orderService.confirmOrder(orderId)).thenReturn(orderDTO);

        // 执行被测方法
        mockMvc.perform(put("/api/orders/{id}/confirm", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.orderNo").value("ORD-20240101-00001"))
                .andExpect(jsonPath("$.status").value(5));

        // 验证结果
        verify(orderService).confirmOrder(orderId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCalculateOrderAmount() throws Exception {
        // 准备测试数据
        Long userId = 1L;

        // 配置mock行为
        when(orderService.calculateOrderAmount(userId)).thenReturn(new BigDecimal(200.0));

        // 执行被测方法
        mockMvc.perform(get("/api/orders/calculateAmount")
                .param("userId", String.valueOf(userId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("200.0"));

        // 验证结果
        verify(orderService).calculateOrderAmount(userId);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testCheckOrderStatus() throws Exception {
        // 准备测试数据
        Long orderId = 1L;

        // 配置mock行为
        when(orderService.checkOrderStatus(orderId)).thenReturn(1); // 1表示待支付状态

        // 执行被测方法
        mockMvc.perform(get("/api/orders/{id}/status", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        // 验证结果
        verify(orderService).checkOrderStatus(orderId);
    }
}