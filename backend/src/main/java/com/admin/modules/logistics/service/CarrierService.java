package com.admin.modules.logistics.service;

import com.admin.common.exception.ResourceNotFoundException;
import com.admin.modules.logistics.dto.CarrierCreateRequest;
import com.admin.modules.logistics.dto.CarrierDto;
import com.admin.modules.logistics.entity.Carrier;
import com.admin.modules.logistics.repository.CarrierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CarrierService {
    
    private final CarrierRepository carrierRepository;
    
    public CarrierDto createCarrier(CarrierCreateRequest request) {
        if (carrierRepository.findByCode(request.getCode()).isPresent()) {
            throw new IllegalArgumentException("承运商编码已存在");
        }
        
        Carrier carrier = new Carrier();
        carrier.setName(request.getName());
        carrier.setCode(request.getCode());
        carrier.setDescription(request.getDescription());
        carrier.setContactPerson(request.getContactPerson());
        carrier.setContactPhone(request.getContactPhone());
        carrier.setContactEmail(request.getContactEmail());
        carrier.setAddress(request.getAddress());
        carrier.setBasePrice(request.getBasePrice().doubleValue());
        carrier.setPricePerKg(request.getPricePerKg().doubleValue());
        carrier.setPricePerKm(request.getPricePerKm().doubleValue());
        
        carrier = carrierRepository.save(carrier);
        return convertToDto(carrier);
    }
    
    @Transactional(readOnly = true)
    public CarrierDto getCarrierById(Long id) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("承运商不存在"));
        return convertToDto(carrier);
    }
    
    @Transactional(readOnly = true)
    public List<CarrierDto> getActiveCarriers() {
        return carrierRepository.findByIsActiveTrueAndIsDeletedFalse()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<CarrierDto> getAllCarriers(Pageable pageable) {
        return carrierRepository.findByIsDeletedFalse(pageable)
                .map(this::convertToDto);
    }
    
    @Transactional(readOnly = true)
    public Page<CarrierDto> searchCarriers(String keyword, Pageable pageable) {
        return carrierRepository.searchByKeyword(keyword, pageable)
                .map(this::convertToDto);
    }
    
    public CarrierDto updateCarrier(Long id, CarrierCreateRequest request) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("承运商不存在"));
        
        carrier.setName(request.getName());
        carrier.setDescription(request.getDescription());
        carrier.setContactPerson(request.getContactPerson());
        carrier.setContactPhone(request.getContactPhone());
        carrier.setContactEmail(request.getContactEmail());
        carrier.setAddress(request.getAddress());
        carrier.setBasePrice(request.getBasePrice().doubleValue());
        carrier.setPricePerKg(request.getPricePerKg().doubleValue());
        carrier.setPricePerKm(request.getPricePerKm().doubleValue());
        
        carrier = carrierRepository.save(carrier);
        return convertToDto(carrier);
    }
    
    public void deleteCarrier(Long id) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("承运商不存在"));
        carrier.setIsDeleted(true);
        carrierRepository.save(carrier);
    }
    
    public CarrierDto toggleCarrierStatus(Long id) {
        Carrier carrier = carrierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("承运商不存在"));
        carrier.setIsActive(!carrier.getIsActive());
        carrier = carrierRepository.save(carrier);
        return convertToDto(carrier);
    }
    
    private CarrierDto convertToDto(Carrier carrier) {
        CarrierDto dto = new CarrierDto();
        dto.setId(carrier.getId());
        dto.setName(carrier.getName());
        dto.setCode(carrier.getCode());
        dto.setDescription(carrier.getDescription());
        dto.setContactPerson(carrier.getContactPerson());
        dto.setContactPhone(carrier.getContactPhone());
        dto.setContactEmail(carrier.getContactEmail());
        dto.setAddress(carrier.getAddress());
        dto.setIsActive(carrier.getIsActive());
        dto.setBasePrice(BigDecimal.valueOf(carrier.getBasePrice()));
        dto.setPricePerKg(BigDecimal.valueOf(carrier.getPricePerKg()));
        dto.setPricePerKm(BigDecimal.valueOf(carrier.getPricePerKm()));
        return dto;
    }
}