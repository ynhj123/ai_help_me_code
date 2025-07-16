package com.admin.modules.logistics.dto;

import com.admin.modules.logistics.enums.DeliveryMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShippingOrderCreateRequest {
    
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    @NotNull(message = "用户ID不能为空")
    private Long userId;
    
    @NotNull(message = "配送方式不能为空")
    private DeliveryMethod deliveryMethod;
    
    private Long carrierId;
    
    private String localDeliveryPerson;
    
    private String localDeliveryPhone;
    
    private String pickupAddress;
    
    private String pickupInstructions;
    
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;
    
    @NotBlank(message = "收货人电话不能为空")
    private String receiverPhone;
    
    @NotBlank(message = "收货地址不能为空")
    private String receiverAddress;
    
    private BigDecimal weight;
    
    private BigDecimal volume;
}