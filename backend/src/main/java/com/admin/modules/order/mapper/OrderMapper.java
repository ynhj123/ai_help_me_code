package com.admin.modules.order.mapper;

import com.admin.modules.order.dto.OrderAddressDto;
import com.admin.modules.order.dto.OrderDto;
import com.admin.modules.order.dto.OrderItemDto;
import com.admin.modules.order.entity.Order;
import com.admin.modules.order.entity.OrderAddress;
import com.admin.modules.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    
    OrderDto toDto(Order order);
    
    OrderItemDto toItemDto(OrderItem item);
    
    OrderAddressDto toAddressDto(OrderAddress address);
    
    List<OrderItemDto> toItemDtoList(List<OrderItem> items);
}