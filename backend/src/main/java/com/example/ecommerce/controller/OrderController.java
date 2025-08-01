package com.example.ecommerce.controller;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.dto.OrderQueryRequest;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     *
     * @param orderDTO 订单信息
     * @param userDetails 当前用户
     * @return 创建的订单
     */
    @PostMapping
    public ResultVO<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO, 
                                        @AuthenticationPrincipal UserDetails userDetails) {
        // 设置用户ID
        orderDTO.setUserId(Long.parseLong(userDetails.getUsername()));
        
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResultVO.success("订单创建成功", createdOrder);
    }

    /**
     * 获取订单详情
     *
     * @param id 订单ID
     * @param userDetails 当前用户
     * @return 订单详情
     */
    @GetMapping("/{id}")
    public ResultVO<OrderDTO> getOrder(@PathVariable Long id, 
                                     @AuthenticationPrincipal UserDetails userDetails) {
        // 检查订单是否属于当前用户
        if (!orderService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权访问该订单");
        }
        
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResultVO.success(orderDTO);
    }

    /**
     * 根据订单编号获取订单
     *
     * @param orderNo 订单编号
     * @param userDetails 当前用户
     * @return 订单详情
     */
    @GetMapping("/no/{orderNo}")
    public ResultVO<OrderDTO> getOrderByNo(@PathVariable String orderNo, 
                                         @AuthenticationPrincipal UserDetails userDetails) {
        OrderDTO orderDTO = orderService.getOrderByOrderNo(orderNo);
        
        // 检查订单是否属于当前用户
        if (!orderService.belongsToUser(orderDTO.getId(), Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权访问该订单");
        }
        
        return ResultVO.success(orderDTO);
    }

    /**
     * 获取当前用户的订单列表
     *
     * @param userDetails 当前用户
     * @return 订单列表
     */
    @GetMapping("/my")
    public ResultVO<List<OrderDTO>> getMyOrders(@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderDTO> orderList = orderService.getOrdersByUserId(Long.parseLong(userDetails.getUsername()));
        return ResultVO.success(orderList);
    }

    /**
     * 获取订单列表（分页）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 订单列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<OrderDTO>> getOrderList(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        List<OrderDTO> orderList = orderService.getOrderList(page, size);
        return ResultVO.success(orderList);
    }

    /**
     * 条件查询订单列表
     *
     * @param queryRequest 查询请求
     * @return 订单列表
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<List<OrderDTO>> searchOrders(@Valid OrderQueryRequest queryRequest) {
        List<OrderDTO> orderList = orderService.getOrderList(queryRequest);
        return ResultVO.success(orderList);
    }

    /**
     * 获取订单总数
     *
     * @param queryRequest 查询请求
     * @return 总数
     */
    @GetMapping("/count")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Integer> getOrderCount(@Valid OrderQueryRequest queryRequest) {
        int count = orderService.getOrderCount(queryRequest);
        return ResultVO.success(count);
    }

    /**
     * 更新订单
     *
     * @param id       订单ID
     * @param orderDTO 订单信息
     * @return 更新后的订单
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<OrderDTO> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResultVO.success("订单更新成功", updatedOrder);
    }

    /**
     * 删除订单
     *
     * @param id 订单ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResultVO.success("订单删除成功", null);
    }

    /**
     * 取消订单
     *
     * @param id          订单ID
     * @param cancelReason 取消原因
     * @param userDetails 当前用户
     * @return 更新后的订单
     */
    @PutMapping("/{id}/cancel")
    public ResultVO<OrderDTO> cancelOrder(@PathVariable Long id, 
                                        @RequestParam(required = false) String cancelReason,
                                        @AuthenticationPrincipal UserDetails userDetails) {
        // 检查订单是否属于当前用户
        if (!orderService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该订单");
        }
        
        OrderDTO orderDTO = orderService.cancelOrder(id, cancelReason);
        return ResultVO.success("订单取消成功", orderDTO);
    }

    /**
     * 支付订单
     *
     * @param id 订单ID
     * @param userDetails 当前用户
     * @return 更新后的订单
     */
    @PutMapping("/{id}/pay")
    public ResultVO<OrderDTO> payOrder(@PathVariable Long id, 
                                     @AuthenticationPrincipal UserDetails userDetails) {
        // 检查订单是否属于当前用户
        if (!orderService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该订单");
        }
        
        OrderDTO orderDTO = orderService.payOrder(id);
        return ResultVO.success("订单支付成功", orderDTO);
    }

    /**
     * 发货
     *
     * @param id               订单ID
     * @param logisticsCompany 物流公司
     * @param trackingNumber   物流单号
     * @return 更新后的订单
     */
    @PutMapping("/{id}/ship")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<OrderDTO> shipOrder(@PathVariable Long id, 
                                      @RequestParam String logisticsCompany,
                                      @RequestParam String trackingNumber) {
        OrderDTO orderDTO = orderService.shipOrder(id, logisticsCompany, trackingNumber);
        return ResultVO.success("订单发货成功", orderDTO);
    }

    /**
     * 确认收货
     *
     * @param id 订单ID
     * @param userDetails 当前用户
     * @return 更新后的订单
     */
    @PutMapping("/{id}/confirm")
    public ResultVO<OrderDTO> confirmOrder(@PathVariable Long id, 
                                         @AuthenticationPrincipal UserDetails userDetails) {
        // 检查订单是否属于当前用户
        if (!orderService.belongsToUser(id, Long.parseLong(userDetails.getUsername()))) {
            throw new org.springframework.security.access.AccessDeniedException("无权操作该订单");
        }
        
        OrderDTO orderDTO = orderService.confirmOrder(id);
        return ResultVO.success("订单确认成功", orderDTO);
    }

    /**
     * 更新订单状态
     *
     * @param id     订单ID
     * @param status 订单状态
     * @return 更新后的订单
     */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestParam Integer status) {
        OrderDTO orderDTO = orderService.updateOrderStatus(id, status);
        return ResultVO.success("订单状态更新成功", orderDTO);
    }

    /**
     * 更新支付状态
     *
     * @param id     订单ID
     * @param paidAt 支付时间
     * @param status 订单状态
     * @return 更新后的订单
     */
    @PutMapping("/{id}/payment-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<OrderDTO> updatePaymentStatus(@PathVariable Long id, 
                                                @RequestParam java.time.LocalDateTime paidAt, 
                                                @RequestParam Integer status) {
        OrderDTO orderDTO = orderService.updatePaymentStatus(id, paidAt, status);
        return ResultVO.success("支付状态更新成功", orderDTO);
    }

    /**
     * 更新发货状态
     *
     * @param id               订单ID
     * @param logisticsCompany 物流公司
     * @param trackingNumber   物流单号
     * @param shippedAt        发货时间
     * @param status           订单状态
     * @return 更新后的订单
     */
    @PutMapping("/{id}/shipping-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<OrderDTO> updateShippingStatus(@PathVariable Long id, 
                                                  @RequestParam String logisticsCompany,
                                                  @RequestParam String trackingNumber,
                                                  @RequestParam java.time.LocalDateTime shippedAt, 
                                                  @RequestParam Integer status) {
        OrderDTO orderDTO = orderService.updateShippingStatus(id, logisticsCompany, trackingNumber, shippedAt, status);
        return ResultVO.success("发货状态更新成功", orderDTO);
    }

    /**
     * 更新完成状态
     *
     * @param id         订单ID
     * @param completedAt 完成时间
     * @param status     订单状态
     * @return 更新后的订单
     */
    @PutMapping("/{id}/completed-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<OrderDTO> updateCompletedStatus(@PathVariable Long id, 
                                                   @RequestParam java.time.LocalDateTime completedAt, 
                                                   @RequestParam Integer status) {
        OrderDTO orderDTO = orderService.updateCompletedStatus(id, completedAt, status);
        return ResultVO.success("完成状态更新成功", orderDTO);
    }

    /**
     * 更新取消状态
     *
     * @param id          订单ID
     * @param cancelledAt 取消时间
     * @param cancelReason 取消原因
     * @param status      订单状态
     * @return 更新后的订单
     */
    @PutMapping("/{id}/cancelled-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<OrderDTO> updateCancelledStatus(@PathVariable Long id, 
                                                   @RequestParam java.time.LocalDateTime cancelledAt, 
                                                   @RequestParam String cancelReason, 
                                                   @RequestParam Integer status) {
        OrderDTO orderDTO = orderService.updateCancelledStatus(id, cancelledAt, cancelReason, status);
        return ResultVO.success("取消状态更新成功", orderDTO);
    }

    /**
     * 检查订单是否存在
     *
     * @param id 订单ID
     * @return 是否存在
     */
    @GetMapping("/{id}/exists")
    public ResultVO<Boolean> existsById(@PathVariable Long id) {
        boolean exists = orderService.existsById(id);
        return ResultVO.success(exists);
    }

    /**
     * 检查订单是否存在
     *
     * @param orderNo 订单编号
     * @return 是否存在
     */
    @GetMapping("/no/{orderNo}/exists")
    public ResultVO<Boolean> existsByOrderNo(@PathVariable String orderNo) {
        boolean exists = orderService.existsByOrderNo(orderNo);
        return ResultVO.success(exists);
    }

    /**
     * 检查订单是否属于指定用户
     *
     * @param id     订单ID
     * @param userId 用户ID
     * @return 是否属于
     */
    @GetMapping("/{id}/belongs-to-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultVO<Boolean> belongsToUser(@PathVariable Long id, @PathVariable Long userId) {
        boolean belongs = orderService.belongsToUser(id, userId);
        return ResultVO.success(belongs);
    }

    /**
     * 检查订单状态是否可以取消
     *
     * @param status 订单状态
     * @return 是否可以取消
     */
    @GetMapping("/status/{status}/can-cancel")
    public ResultVO<Boolean> canBeCancelled(@PathVariable Integer status) {
        boolean canCancel = orderService.canBeCancelled(status);
        return ResultVO.success(canCancel);
    }

    /**
     * 检查订单状态是否可以支付
     *
     * @param status 订单状态
     * @return 是否可以支付
     */
    @GetMapping("/status/{status}/can-pay")
    public ResultVO<Boolean> canBePaid(@PathVariable Integer status) {
        boolean canPay = orderService.canBePaid(status);
        return ResultVO.success(canPay);
    }

    /**
     * 检查订单状态是否可以发货
     *
     * @param status 订单状态
     * @return 是否可以发货
     */
    @GetMapping("/status/{status}/can-ship")
    public ResultVO<Boolean> canBeShipped(@PathVariable Integer status) {
        boolean canShip = orderService.canBeShipped(status);
        return ResultVO.success(canShip);
    }

    /**
     * 检查订单状态是否可以确认收货
     *
     * @param status 订单状态
     * @return 是否可以确认收货
     */
    @GetMapping("/status/{status}/can-confirm")
    public ResultVO<Boolean> canBeConfirmed(@PathVariable Integer status) {
        boolean canConfirm = orderService.canBeConfirmed(status);
        return ResultVO.success(canConfirm);
    }
}