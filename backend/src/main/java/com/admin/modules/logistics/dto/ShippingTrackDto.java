package com.admin.modules.logistics.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShippingTrackDto {
    private Long id;
    private Long shippingOrderId;
    private String status;
    private String description;
    private String location;
    private LocalDateTime timestamp;
    private String notes;
}