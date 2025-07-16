package com.admin.modules.order.dto;

import lombok.Data;

@Data
public class OrderAddressDto {
    private Long id;
    private String receiverName;
    private String receiverPhone;
    private String province;
    private String city;
    private String district;
    private String address;
    private String postalCode;
    private String remark;
}