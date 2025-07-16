package com.admin.modules.logistics.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarrierDto {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String contactPerson;
    private String contactPhone;
    private String contactEmail;
    private String address;
    private Boolean isActive;
    private BigDecimal basePrice;
    private BigDecimal pricePerKg;
    private BigDecimal pricePerKm;
}