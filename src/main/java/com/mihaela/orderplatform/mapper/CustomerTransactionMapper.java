package com.mihaela.orderplatform.mapper;

import com.mihaela.orderplatform.domain.CompositeKey;
import com.mihaela.orderplatform.domain.PublishedOrderEvent;
import com.mihaela.orderplatform.dto.CustomerTransactionDto;
import com.mihaela.orderplatform.domain.CustomerTransactions;
import org.springframework.stereotype.Component;

@Component
public class CustomerTransactionMapper {


    public CustomerTransactionDto toDto(CustomerTransactions entity) {

        if (entity == null) {
            return null;
        }

        return CustomerTransactionDto.builder()
                .id(entity.getId())
                .customerId(entity.getCustomerId())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }


    public CustomerTransactions toEntity(CustomerTransactionDto dto) {

        if (dto == null) {
            return null;
        }

        CustomerTransactions entity = new CustomerTransactions();

        entity.setId(dto.getId());
        entity.setCustomerId(dto.getCustomerId());
        entity.setAmount(dto.getAmount());
        entity.setCurrency(dto.getCurrency());
        entity.setStatus(dto.getStatus());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        return entity;
    }

   public PublishedOrderEvent toEvent (CustomerTransactionDto dto) {
       String orderId = new CompositeKey(dto.getId(), dto.getCustomerId()).asString();
       return new PublishedOrderEvent(orderId, dto.getStatus(),
               dto.getAmount(), dto.getCurrency(), dto.getCreatedAt());
   }
}