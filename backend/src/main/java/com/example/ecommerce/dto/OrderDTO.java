package com.example.ecommerce.dto;

import com.example.ecommerce.enums.OrderStatus;
import com.example.ecommerce.enums.PaymentMethod;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单DTO类
 */
@Data
public class OrderDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 订单编号
     */
    @NotBlank(message = "订单编号不能为空")
    private String orderNo;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 收货人姓名
     */
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max = 50, message = "收货人姓名长度不能超过50个字符")
    private String receiverName;

    /**
     * 收货人手机号
     */
    @NotBlank(message = "收货人手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;

    /**
     * 收货地址
     */
    @NotBlank(message = "收货地址不能为空")
    @Size(max = 255, message = "收货地址长度不能超过255个字符")
    private String receiverAddress;

    /**
     * 订单总金额
     */
    @NotNull(message = "订单总金额不能为空")
    @DecimalMin(value = "0.01", message = "订单总金额必须大于0")
    private BigDecimal totalAmount;

    /**
     * 商品总金额
     */
    @NotNull(message = "商品总金额不能为空")
    @DecimalMin(value = "0.01", message = "商品总金额必须大于0")
    private BigDecimal productAmount;

    /**
     * 运费
     */
    @NotNull(message = "运费不能为空")
    @DecimalMin(value = "0.00", message = "运费不能小于0")
    private BigDecimal shippingFee;

    /**
     * 优惠金额
     */
    @NotNull(message = "优惠金额不能为空")
    @DecimalMin(value = "0.00", message = "优惠金额不能小于0")
    private BigDecimal discountAmount;

    /**
     * 实付金额
     */
    @NotNull(message = "实付金额不能为空")
    @DecimalMin(value = "0.01", message = "实付金额必须大于0")
    private BigDecimal paidAmount;

    /**
     * 支付方式
     */
    @NotNull(message = "支付方式不能为空")
    private PaymentMethod paymentMethod;

    /**
     * 支付时间
     */
    private LocalDateTime paidAt;

    /**
     * 订单状态
     */
    @NotNull(message = "订单状态不能为空")
    private OrderStatus status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 物流公司
     */
    private String logisticsCompany;

    /**
     * 物流单号
     */
    private String trackingNumber;

    /**
     * 发货时间
     */
    private LocalDateTime shippedAt;

    /**
     * 完成时间
     */
    private LocalDateTime completedAt;

    /**
     * 取消时间
     */
    private LocalDateTime cancelledAt;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 订单商品列表
     */
    private List<OrderItemDTO> orderItems;
}