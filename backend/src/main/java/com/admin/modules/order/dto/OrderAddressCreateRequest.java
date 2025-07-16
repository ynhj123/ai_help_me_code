package com.admin.modules.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderAddressCreateRequest {
    
    @NotBlank(message = "收货人姓名不能为空")
    private String receiverName;
    
    @NotBlank(message = "收货人电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String receiverPhone;
    
    @NotBlank(message = "省份不能为空")
    private String province;
    
    @NotBlank(message = "城市不能为空")
    private String city;
    
    @NotBlank(message = "区县不能为空")
    private String district;
    
    @NotBlank(message = "详细地址不能为空")
    private String address;
    
    @Pattern(regexp = "^\\d{6}$", message = "邮政编码格式不正确")
    private String postalCode;
    
    private String remark;
}