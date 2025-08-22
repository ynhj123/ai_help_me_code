package com.example.ecommerce.mapper;

import com.example.ecommerce.entity.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 订单Mapper接口
 */
@Mapper
public interface OrderMapper {

    /**
     * 插入订单
     *
     * @param order 订单
     * @return 影响行数
     */
    int insert(Order order);

    /**
     * 根据ID查询订单
     *
     * @param id 订单ID
     * @return 订单
     */
    Order selectById(Long id);

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo 订单编号
     * @return 订单
     */
    Order selectByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    List<Order> selectByUserId(Long userId);

    /**
     * 查询订单列表（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 订单列表
     */
    List<Order> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询订单列表（带条件）
     *
     * @param offset     偏移量
     * @param limit      限制数量
     * @param orderNo    订单编号
     * @param userId     用户ID
     * @param status     订单状态
     * @param paymentMethod 支付方式
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param sortBy     排序字段
     * @param sortOrder  排序方式
     * @return 订单列表
     */
    List<Order> selectAll(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("orderNo") String orderNo,
            @Param("userId") Long userId,
            @Param("status") Integer status,
            @Param("paymentMethod") Integer paymentMethod,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime,
            @Param("sortBy") String sortBy,
            @Param("sortOrder") String sortOrder
    );

    /**
     * 查询订单总数
     *
     * @param orderNo    订单编号
     * @param userId     用户ID
     * @param status     订单状态
     * @param paymentMethod 支付方式
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @return 总数
     */
    int count(
            @Param("orderNo") String orderNo,
            @Param("userId") Long userId,
            @Param("status") Integer status,
            @Param("paymentMethod") Integer paymentMethod,
            @Param("startTime") java.time.LocalDateTime startTime,
            @Param("endTime") java.time.LocalDateTime endTime
    );

    /**
     * 更新订单
     *
     * @param order 订单
     * @return 影响行数
     */
    int update(Order order);

    /**
     * 根据ID删除订单
     *
     * @param id 订单ID
     * @return 影响行数
     */
    int deleteById(Long id);

    /**
     * 根据订单编号删除订单
     *
     * @param orderNo 订单编号
     * @return 影响行数
     */
    int deleteByOrderNo(String orderNo);

    /**
     * 更新订单状态
     *
     * @param id     订单ID
     * @param status 订单状态
     * @return 影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新支付状态
     *
     * @param id       订单ID
     * @param paidAt   支付时间
     * @param status   订单状态
     * @return 影响行数
     */
    int updatePaymentStatus(@Param("id") Long id, @Param("paidAt") java.time.LocalDateTime paidAt, @Param("status") Integer status);

    /**
     * 更新发货状态
     *
     * @param id               订单ID
     * @param logisticsCompany 物流公司
     * @param trackingNumber   物流单号
     * @param shippedAt        发货时间
     * @param status           订单状态
     * @return 影响行数
     */
    int updateShippingStatus(@Param("id") Long id, @Param("logisticsCompany") String logisticsCompany, 
                            @Param("trackingNumber") String trackingNumber, @Param("shippedAt") java.time.LocalDateTime shippedAt, 
                            @Param("status") Integer status);

    /**
     * 更新完成状态
     *
     * @param id         订单ID
     * @param completedAt 完成时间
     * @param status     订单状态
     * @return 影响行数
     */
    int updateCompletedStatus(@Param("id") Long id, @Param("completedAt") java.time.LocalDateTime completedAt, @Param("status") Integer status);

    /**
     * 更新取消状态
     *
     * @param id          订单ID
     * @param cancelledAt 取消时间
     * @param cancelReason 取消原因
     * @param status      订单状态
     * @return 影响行数
     */
    int updateCancelledStatus(@Param("id") Long id, @Param("cancelledAt") java.time.LocalDateTime cancelledAt, 
                             @Param("cancelReason") String cancelReason, @Param("status") Integer status);
}