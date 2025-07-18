package com.admin.modules.logistics.service;

import com.admin.common.exception.ResourceNotFoundException;
import com.admin.modules.logistics.dto.CarrierDto;
import com.admin.modules.logistics.dto.ShippingOrderCreateRequest;
import com.admin.modules.logistics.dto.ShippingOrderDto;
import com.admin.modules.logistics.dto.ShippingTrackDto;
import com.admin.modules.logistics.entity.Carrier;
import com.admin.modules.logistics.entity.ShippingOrder;
import com.admin.modules.logistics.entity.ShippingTrack;
import com.admin.modules.logistics.enums.ShippingStatus;
import com.admin.modules.logistics.repository.CarrierRepository;
import com.admin.modules.logistics.repository.ShippingOrderRepository;
import com.admin.modules.logistics.repository.ShippingTrackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ShippingOrderService {
    
    private final ShippingOrderRepository shippingOrderRepository;
    private final ShippingTrackRepository shippingTrackRepository;
    private final CarrierRepository carrierRepository;
    
    public ShippingOrderDto createShippingOrder(ShippingOrderCreateRequest request) {
        ShippingOrder shippingOrder = new ShippingOrder();
        
        // 生成运单号
        String trackingNumber = generateTrackingNumber();
        shippingOrder.setTrackingNumber(trackingNumber);
        
        shippingOrder.setOrderId(request.getOrderId());
        shippingOrder.setUserId(request.getUserId());
        shippingOrder.setDeliveryMethod(request.getDeliveryMethod());
        
        // 根据配送方式设置不同信息
        switch (request.getDeliveryMethod()) {
            case CARRIER:
                if (request.getCarrierId() != null) {
                    Carrier carrier = carrierRepository.findById(request.getCarrierId())
                            .orElseThrow(() -> new ResourceNotFoundException("承运商不存在"));
                    shippingOrder.setCarrier(carrier);
                    
                    // 计算运费
                    BigDecimal shippingFee = calculateShippingFee(carrier, request.getWeight(), request.getVolume());
                    shippingOrder.setShippingFee(shippingFee);
                }
                break;
            case LOCAL:
                shippingOrder.setLocalDeliveryPerson(request.getLocalDeliveryPerson());
                shippingOrder.setLocalDeliveryPhone(request.getLocalDeliveryPhone());
                shippingOrder.setShippingFee(BigDecimal.valueOf(10.00)); // 本地配送固定费用
                break;
            case SELF_PICKUP:
                shippingOrder.setPickupAddress(request.getPickupAddress());
                shippingOrder.setPickupInstructions(request.getPickupInstructions());
                shippingOrder.setShippingFee(BigDecimal.ZERO); // 自取免费
                break;
        }
        
        shippingOrder.setReceiverName(request.getReceiverName());
        shippingOrder.setReceiverPhone(request.getReceiverPhone());
        shippingOrder.setReceiverAddress(request.getReceiverAddress());
        shippingOrder.setWeight(request.getWeight());
        shippingOrder.setVolume(request.getVolume());
        
        shippingOrder = shippingOrderRepository.save(shippingOrder);
        
        // 创建初始跟踪记录
        createInitialTrack(shippingOrder);
        
        return convertToDto(shippingOrder);
    }
    
    @Transactional(readOnly = true)
    public ShippingOrderDto getShippingOrderById(Long id) {
        ShippingOrder shippingOrder = shippingOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("运单不存在"));
        return convertToDto(shippingOrder);
    }
    
    @Transactional(readOnly = true)
    public ShippingOrderDto getShippingOrderByTrackingNumber(String trackingNumber) {
        ShippingOrder shippingOrder = shippingOrderRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(() -> new ResourceNotFoundException("运单不存在"));
        return convertToDto(shippingOrder);
    }
    
    @Transactional(readOnly = true)
    public List<ShippingOrderDto> getShippingOrdersByOrderId(Long orderId) {
        return shippingOrderRepository.findByOrderId(orderId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ShippingOrderDto> getShippingOrdersByUserId(Long userId, Pageable pageable) {
        return shippingOrderRepository.findByUserIdAndIsDeletedFalse(userId, pageable)
                .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<ShippingOrderDto> getAllShippingOrders(Pageable pageable) {
        return shippingOrderRepository.findAll(pageable)
                .map(this::convertToDto);
    }
    
    public ShippingOrderDto updateShippingStatus(Long id, ShippingStatus status, String notes) {
        ShippingOrder shippingOrder = shippingOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("运单不存在"));
        
        shippingOrder.setStatus(status);
        
        // 设置相应时间
        LocalDateTime now = LocalDateTime.now();
        switch (status) {
            case SHIPPED:
                shippingOrder.setShippedAt(now);
                break;
            case DELIVERED:
                shippingOrder.setDeliveredAt(now);
                break;
        }
        
        shippingOrder = shippingOrderRepository.save(shippingOrder);
        
        // 添加跟踪记录
        createTrackRecord(shippingOrder, status, notes);
        
        return convertToDto(shippingOrder);
    }
    
    public ShippingTrackDto addTrackRecord(Long shippingOrderId, String status, String description, String location, String notes) {
        ShippingOrder shippingOrder = shippingOrderRepository.findById(shippingOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("运单不存在"));
        
        ShippingTrack track = new ShippingTrack();
        track.setShippingOrder(shippingOrder);
        track.setStatus(status);
        track.setDescription(description);
        track.setLocation(location);
        track.setTimestamp(LocalDateTime.now());
        track.setNotes(notes);
        
        track = shippingTrackRepository.save(track);
        
        return convertTrackToDto(track);
    }
    
    private String generateTrackingNumber() {
        return "TRK" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 4).toUpperCase();
    }
    
    private BigDecimal calculateShippingFee(Carrier carrier, BigDecimal weight, BigDecimal volume) {
        if (weight == null) weight = BigDecimal.ZERO;
        if (volume == null) volume = BigDecimal.ZERO;
        
        double basePrice = carrier.getBasePrice();
        double weightPrice = weight.doubleValue() * carrier.getPricePerKg();
        double volumePrice = volume.doubleValue() * carrier.getPricePerKm();
        
        return BigDecimal.valueOf(basePrice + weightPrice + volumePrice);
    }
    
    private void createInitialTrack(ShippingOrder shippingOrder) {
        ShippingTrack track = new ShippingTrack();
        track.setShippingOrder(shippingOrder);
        track.setStatus("PENDING");
        track.setDescription("运单已创建，等待处理");
        track.setLocation("仓库");
        track.setTimestamp(LocalDateTime.now());
        track.setNotes("运单创建成功");
        
        shippingTrackRepository.save(track);
    }
    
    private void createTrackRecord(ShippingOrder shippingOrder, ShippingStatus status, String notes) {
        ShippingTrack track = new ShippingTrack();
        track.setShippingOrder(shippingOrder);
        track.setStatus(status.name());
        track.setDescription(getStatusDescription(status));
        track.setLocation("物流节点");
        track.setTimestamp(LocalDateTime.now());
        track.setNotes(notes);
        
        shippingTrackRepository.save(track);
    }
    
    private String getStatusDescription(ShippingStatus status) {
        switch (status) {
            case PENDING: return "等待处理";
            case PROCESSING: return "处理中";
            case SHIPPED: return "已发货";
            case IN_TRANSIT: return "运输中";
            case OUT_FOR_DELIVERY: return "派送中";
            case DELIVERED: return "已送达";
            case FAILED: return "配送失败";
            case RETURNED: return "已退回";
            default: return "状态更新";
        }
    }
    
    private ShippingOrderDto convertToDto(ShippingOrder shippingOrder) {
        ShippingOrderDto dto = new ShippingOrderDto();
        dto.setId(shippingOrder.getId());
        dto.setTrackingNumber(shippingOrder.getTrackingNumber());
        dto.setOrderId(shippingOrder.getOrderId());
        dto.setUserId(shippingOrder.getUserId());
        dto.setDeliveryMethod(shippingOrder.getDeliveryMethod());
        
        if (shippingOrder.getCarrier() != null) {
            CarrierDto carrierDto = new CarrierDto();
            carrierDto.setId(shippingOrder.getCarrier().getId());
            carrierDto.setName(shippingOrder.getCarrier().getName());
            carrierDto.setCode(shippingOrder.getCarrier().getCode());
            dto.setCarrier(carrierDto);
        }
        
        dto.setLocalDeliveryPerson(shippingOrder.getLocalDeliveryPerson());
        dto.setLocalDeliveryPhone(shippingOrder.getLocalDeliveryPhone());
        dto.setPickupAddress(shippingOrder.getPickupAddress());
        dto.setPickupInstructions(shippingOrder.getPickupInstructions());
        dto.setReceiverName(shippingOrder.getReceiverName());
        dto.setReceiverPhone(shippingOrder.getReceiverPhone());
        dto.setReceiverAddress(shippingOrder.getReceiverAddress());
        dto.setShippingFee(shippingOrder.getShippingFee());
        dto.setWeight(shippingOrder.getWeight());
        dto.setVolume(shippingOrder.getVolume());
        dto.setStatus(shippingOrder.getStatus());
        dto.setShippedAt(shippingOrder.getShippedAt());
        dto.setEstimatedDeliveryAt(shippingOrder.getEstimatedDeliveryAt());
        dto.setDeliveredAt(shippingOrder.getDeliveredAt());
        dto.setTrackingInfo(shippingOrder.getTrackingInfo());
        dto.setDeliveryNotes(shippingOrder.getDeliveryNotes());
        
        // 添加跟踪记录
        List<ShippingTrackDto> tracks = shippingTrackRepository.findByShippingOrderIdOrderByTimestampDesc(shippingOrder.getId())
                .stream()
                .map(this::convertTrackToDto)
                .collect(Collectors.toList());
        dto.setTracks(tracks);
        
        return dto;
    }
    
    private ShippingTrackDto convertTrackToDto(ShippingTrack track) {
        ShippingTrackDto dto = new ShippingTrackDto();
        dto.setId(track.getId());
        dto.setShippingOrderId(track.getShippingOrder().getId());
        dto.setStatus(track.getStatus());
        dto.setDescription(track.getDescription());
        dto.setLocation(track.getLocation());
        dto.setTimestamp(track.getTimestamp());
        dto.setNotes(track.getNotes());
        return dto;
    }
}