package com.mihaela.orderplatform.mapper;

import com.mihaela.orderplatform.dto.CustomerOrderDto;
import com.mihaela.orderplatform.domain.CustomerOrders;
import org.springframework.stereotype.Component;

@Component
public class CustomerOrderMapper {


    public CustomerOrderDto toDto(CustomerOrders entity) {

        if (entity == null) {
            return null;
        }

        return CustomerOrderDto.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public CustomerOrders toEntity(CustomerOrderDto dto) {

        if (dto == null) {
            return null;
        }

        CustomerOrders entity = new CustomerOrders();

        entity.setId(dto.getId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setAmount(dto.getAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setStatus(dto.getStatus());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }
}