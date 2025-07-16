package com.admin.modules.order.dto;

import com.admin.modules.order.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    
    @NotEmpty(message = "订单商品不能为空")
    private List<OrderItemCreateRequest> items;
    
    @NotNull(message = "收货地址不能为空")
    @Valid
    private OrderAddressCreateRequest address;
    
    private PaymentMethod paymentMethod;
    
    private String remark;
}