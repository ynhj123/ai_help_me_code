package com.admin.modules.order.controller;

import com.admin.common.security.service.UserDetailsImpl;
import com.admin.modules.order.dto.OrderCreateRequest;
import com.admin.modules.order.dto.OrderDto;
import com.admin.modules.order.enums.OrderStatus;
import com.admin.modules.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "订单相关接口")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    @Operation(summary = "创建订单")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDto> createOrder(
            @Valid @RequestBody OrderCreateRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OrderDto order = orderService.createOrder(request, userDetails.getId());
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/{orderNumber}")
    @Operation(summary = "根据订单号获取订单详情")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDto> getOrderByNumber(@PathVariable String orderNumber) {
        OrderDto order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(order);
    }
    
    @GetMapping("/my")
    @Operation(summary = "获取当前用户的订单列表")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<OrderDto>> getMyOrders(
            @PageableDefault(size = 10) Pageable pageable,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<OrderDto> orders = orderService.getUserOrders(userDetails.getId(), pageable);
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping
    @Operation(summary = "获取所有订单（管理员）")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<OrderDto> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }
    
    @PutMapping("/{orderId}/status")
    @Operation(summary = "更新订单状态")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String remark) {
        OrderDto order = orderService.updateOrderStatus(orderId, status, remark);
        return ResponseEntity.ok(order);
    }
    
    @PutMapping("/{orderId}/cancel")
    @Operation(summary = "取消订单")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDto> cancelOrder(
            @PathVariable Long orderId,
            @RequestParam(required = false) String reason,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 验证订单是否属于当前用户
        OrderDto order = orderService.getOrderById(orderId);
        if (!order.getUserId().equals(userDetails.getId())) {
            throw new IllegalArgumentException("只能取消自己的订单");
        }
        
        OrderDto cancelledOrder = orderService.cancelOrder(orderId, reason);
        return ResponseEntity.ok(cancelledOrder);
    }
    
    @DeleteMapping("/{orderId}")
    @Operation(summary = "删除订单（软删除）")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}