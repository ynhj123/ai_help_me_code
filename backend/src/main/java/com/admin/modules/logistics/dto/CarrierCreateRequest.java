package com.admin.modules.logistics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarrierCreateRequest {
    
    @NotBlank(message = "承运商名称不能为空")
    private String name;
    
    @NotBlank(message = "承运商编码不能为空")
    private String code;
    
    private String description;
    
    private String contactPerson;
    
    private String contactPhone;
    
    private String contactEmail;
    
    private String address;
    
    @NotNull(message = "基础价格不能为空")
    private BigDecimal basePrice;
    
    @NotNull(message = "每公斤价格不能为空")
    private BigDecimal pricePerKg;
    
    @NotNull(message = "每公里价格不能为空")
    private BigDecimal pricePerKm;
}