package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderDTO;
import com.example.ecommerce.dto.OrderItemDTO;
import com.example.ecommerce.dto.OrderQueryRequest;

import java.util.List;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param orderDTO 订单信息
     * @return 创建的订单
     */
    OrderDTO createOrder(OrderDTO orderDTO);

    /**
     * 根据ID获取订单
     *
     * @param id 订单ID
     * @return 订单信息
     */
    OrderDTO getOrderById(Long id);

    /**
     * 根据订单编号获取订单
     *
     * @param orderNo 订单编号
     * @return 订单信息
     */
    OrderDTO getOrderByOrderNo(String orderNo);

    /**
     * 根据用户ID获取订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    List<OrderDTO> getOrdersByUserId(Long userId);

    /**
     * 获取订单列表（分页）
     *
     * @param page 页码
     * @param size 每页大小
     * @return 订单列表
     */
    List<OrderDTO> getOrderList(Integer page, Integer size);

    /**
     * 获取订单列表（带条件查询）
     *
     * @param queryRequest 查询请求
     * @return 订单列表
     */
    List<OrderDTO> getOrderList(OrderQueryRequest queryRequest);

    /**
     * 获取订单总数
     *
     * @param queryRequest 查询请求
     * @return 总数
     */
    int getOrderCount(OrderQueryRequest queryRequest);

    /**
     * 更新订单
     *
     * @param id       订单ID
     * @param orderDTO 订单信息
     * @return 更新后的订单
     */
    OrderDTO updateOrder(Long id, OrderDTO orderDTO);

    /**
     * 删除订单
     *
     * @param id 订单ID
     */
    void deleteOrder(Long id);

    /**
     * 取消订单
     *
     * @param id          订单ID
     * @param cancelReason 取消原因
     * @return 更新后的订单
     */
    OrderDTO cancelOrder(Long id, String cancelReason);

    /**
     * 支付订单
     *
     * @param id 订单ID
     * @return 更新后的订单
     */
    OrderDTO payOrder(Long id);

    /**
     * 发货
     *
     * @param id               订单ID
     * @param logisticsCompany 物流公司
     * @param trackingNumber   物流单号
     * @return 更新后的订单
     */
    OrderDTO shipOrder(Long id, String logisticsCompany, String trackingNumber);

    /**
     * 确认收货
     *
     * @param id 订单ID
     * @return 更新后的订单
     */
    OrderDTO confirmOrder(Long id);

    /**
     * 更新订单状态
     *
     * @param id     订单ID
     * @param status 订单状态
     * @return 更新后的订单
     */
    OrderDTO updateOrderStatus(Long id, Integer status);

    /**
     * 更新支付状态
     *
     * @param id     订单ID
     * @param paidAt 支付时间
     * @param status 订单状态
     * @return 更新后的订单
     */
    OrderDTO updatePaymentStatus(Long id, java.time.LocalDateTime paidAt, Integer status);

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
    OrderDTO updateShippingStatus(Long id, String logisticsCompany, String trackingNumber, 
                                 java.time.LocalDateTime shippedAt, Integer status);

    /**
     * 更新完成状态
     *
     * @param id         订单ID
     * @param completedAt 完成时间
     * @param status     订单状态
     * @return 更新后的订单
     */
    OrderDTO updateCompletedStatus(Long id, java.time.LocalDateTime completedAt, Integer status);

    /**
     * 更新取消状态
     *
     * @param id          订单ID
     * @param cancelledAt 取消时间
     * @param cancelReason 取消原因
     * @param status      订单状态
     * @return 更新后的订单
     */
    OrderDTO updateCancelledStatus(Long id, java.time.LocalDateTime cancelledAt, 
                                  String cancelReason, Integer status);

    /**
     * 生成订单编号
     *
     * @return 订单编号
     */
    String generateOrderNo();

    /**
     * 计算订单总金额
     *
     * @param orderItems 订单商品列表
     * @param shippingFee 运费
     * @param discountAmount 优惠金额
     * @return 订单总金额
     */
    java.math.BigDecimal calculateOrderAmount(List<OrderItemDTO> orderItems, 
                                            java.math.BigDecimal shippingFee, 
                                            java.math.BigDecimal discountAmount);

    /**
     * 检查订单是否存在
     *
     * @param id 订单ID
     * @return 是否存在
     */
    boolean existsById(Long id);

    /**
     * 检查订单是否存在
     *
     * @param orderNo 订单编号
     * @return 是否存在
     */
    boolean existsByOrderNo(String orderNo);

    /**
     * 检查订单是否属于指定用户
     *
     * @param id     订单ID
     * @param userId 用户ID
     * @return 是否属于
     */
    boolean belongsToUser(Long id, Long userId);

    /**
     * 检查订单状态是否可以取消
     *
     * @param status 订单状态
     * @return 是否可以取消
     */
    boolean canBeCancelled(Integer status);

    /**
     * 检查订单状态是否可以支付
     *
     * @param status 订单状态
     * @return 是否可以支付
     */
    boolean canBePaid(Integer status);

    /**
     * 检查订单状态是否可以发货
     *
     * @param status 订单状态
     * @return 是否可以发货
     */
    boolean canBeShipped(Integer status);

    /**
     * 检查订单状态是否可以确认收货
     *
     * @param status 订单状态
     * @return 是否可以确认收货
     */
    boolean canBeConfirmed(Integer status);
}