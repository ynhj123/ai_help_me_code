package com.admin.modules.logistics.dto;

import com.admin.modules.logistics.enums.DeliveryMethod;
import com.admin.modules.logistics.enums.ShippingStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ShippingOrderDto {
    private Long id;
    private String trackingNumber;
    private Long orderId;
    private Long userId;
    private DeliveryMethod deliveryMethod;
    private CarrierDto carrier;
    private String localDeliveryPerson;
    private String localDeliveryPhone;
    private String pickupAddress;
    private String pickupInstructions;
    private String receiverName;
    private String receiverPhone;
    private String receiverAddress;
    private BigDecimal shippingFee;
    private BigDecimal weight;
    private BigDecimal volume;
    private ShippingStatus status;
    private LocalDateTime shippedAt;
    private LocalDateTime estimatedDeliveryAt;
    private LocalDateTime deliveredAt;
    private String trackingInfo;
    private String deliveryNotes;
    private List<ShippingTrackDto> tracks;
}