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
    @Insert({
        "<script>",
        "INSERT INTO orders (order_no, user_id, receiver_name, receiver_phone, receiver_address, " +
        "total_amount, product_amount, shipping_fee, discount_amount, paid_amount, payment_method, " +
        "paid_at, status, remark, logistics_company, tracking_number, shipped_at, completed_at, " +
        "cancelled_at, cancel_reason, created_at, updated_at)",
        "VALUES (#{orderNo}, #{userId}, #{receiverName}, #{receiverPhone}, #{receiverAddress}, " +
        "#{totalAmount}, #{productAmount}, #{shippingFee}, #{discountAmount}, #{paidAmount}, " +
        "#{paymentMethod}, #{paidAt}, #{status}, #{remark}, #{logisticsCompany}, #{trackingNumber}, " +
        "#{shippedAt}, #{completedAt}, #{cancelledAt}, #{cancelReason}, #{createdAt}, #{updatedAt})",
        "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Order order);

    /**
     * 根据ID查询订单
     *
     * @param id 订单ID
     * @return 订单
     */
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Order selectById(Long id);

    /**
     * 根据订单编号查询订单
     *
     * @param orderNo 订单编号
     * @return 订单
     */
    @Select("SELECT * FROM orders WHERE order_no = #{orderNo}")
    Order selectByOrderNo(String orderNo);

    /**
     * 根据用户ID查询订单列表
     *
     * @param userId 用户ID
     * @return 订单列表
     */
    @Select("SELECT * FROM orders WHERE user_id = #{userId} ORDER BY id DESC")
    List<Order> selectByUserId(Long userId);

    /**
     * 查询订单列表（分页）
     *
     * @param offset 偏移量
     * @param limit  限制数量
     * @return 订单列表
     */
    @Select("SELECT * FROM orders ORDER BY id DESC LIMIT #{offset}, #{limit}")
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
    @Select("<script>" +
            "SELECT * FROM orders WHERE 1=1" +
            "<if test='orderNo != null and orderNo != \"\"'> AND order_no LIKE CONCAT('%', #{orderNo}, '%')</if>" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='paymentMethod != null'> AND payment_method = #{paymentMethod}</if>" +
            "<if test='startTime != null'> AND created_at >= #{startTime}</if>" +
            "<if test='endTime != null'> AND created_at <= #{endTime}</if>" +
            "ORDER BY ${sortBy} ${sortOrder} LIMIT #{offset}, #{limit}" +
            "</script>")
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
    @Select("<script>" +
            "SELECT COUNT(*) FROM orders WHERE 1=1" +
            "<if test='orderNo != null and orderNo != \"\"'> AND order_no LIKE CONCAT('%', #{orderNo}, '%')</if>" +
            "<if test='userId != null'> AND user_id = #{userId}</if>" +
            "<if test='status != null'> AND status = #{status}</if>" +
            "<if test='paymentMethod != null'> AND payment_method = #{paymentMethod}</if>" +
            "<if test='startTime != null'> AND created_at >= #{startTime}</if>" +
            "<if test='endTime != null'> AND created_at <= #{endTime}</if>" +
            "</script>")
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
    @Update({
        "<script>",
        "UPDATE orders",
        "<set>",
        "<if test='orderNo != null'>order_no = #{orderNo},</if>",
        "<if test='userId != null'>user_id = #{userId},</if>",
        "<if test='receiverName != null'>receiver_name = #{receiverName},</if>",
        "<if test='receiverPhone != null'>receiver_phone = #{receiverPhone},</if>",
        "<if test='receiverAddress != null'>receiver_address = #{receiverAddress},</if>",
        "<if test='totalAmount != null'>total_amount = #{totalAmount},</if>",
        "<if test='productAmount != null'>product_amount = #{productAmount},</if>",
        "<if test='shippingFee != null'>shipping_fee = #{shippingFee},</if>",
        "<if test='discountAmount != null'>discount_amount = #{discountAmount},</if>",
        "<if test='paidAmount != null'>paid_amount = #{paidAmount},</if>",
        "<if test='paymentMethod != null'>payment_method = #{paymentMethod},</if>",
        "<if test='paidAt != null'>paid_at = #{paidAt},</if>",
        "<if test='status != null'>status = #{status},</if>",
        "<if test='remark != null'>remark = #{remark},</if>",
        "<if test='logisticsCompany != null'>logistics_company = #{logisticsCompany},</if>",
        "<if test='trackingNumber != null'>tracking_number = #{trackingNumber},</if>",
        "<if test='shippedAt != null'>shipped_at = #{shippedAt},</if>",
        "<if test='completedAt != null'>completed_at = #{completedAt},</if>",
        "<if test='cancelledAt != null'>cancelled_at = #{cancelledAt},</if>",
        "<if test='cancelReason != null'>cancel_reason = #{cancelReason},</if>",
        "updated_at = #{updatedAt}",
        "</set>",
        "WHERE id = #{id}",
        "</script>"
    })
    int update(Order order);

    /**
     * 根据ID删除订单
     *
     * @param id 订单ID
     * @return 影响行数
     */
    @Delete("DELETE FROM orders WHERE id = #{id}")
    int deleteById(Long id);

    /**
     * 根据订单编号删除订单
     *
     * @param orderNo 订单编号
     * @return 影响行数
     */
    @Delete("DELETE FROM orders WHERE order_no = #{orderNo}")
    int deleteByOrderNo(String orderNo);

    /**
     * 更新订单状态
     *
     * @param id     订单ID
     * @param status 订单状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 更新支付状态
     *
     * @param id       订单ID
     * @param paidAt   支付时间
     * @param status   订单状态
     * @return 影响行数
     */
    @Update("UPDATE orders SET paid_at = #{paidAt}, status = #{status}, updated_at = NOW() WHERE id = #{id}")
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
    @Update("UPDATE orders SET logistics_company = #{logisticsCompany}, tracking_number = #{trackingNumber}, " +
            "shipped_at = #{shippedAt}, status = #{status}, updated_at = NOW() WHERE id = #{id}")
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
    @Update("UPDATE orders SET completed_at = #{completedAt}, status = #{status}, updated_at = NOW() WHERE id = #{id}")
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
    @Update("UPDATE orders SET cancelled_at = #{cancelledAt}, cancel_reason = #{cancelReason}, " +
            "status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int updateCancelledStatus(@Param("id") Long id, @Param("cancelledAt") java.time.LocalDateTime cancelledAt, 
                             @Param("cancelReason") String cancelReason, @Param("status") Integer status);
}