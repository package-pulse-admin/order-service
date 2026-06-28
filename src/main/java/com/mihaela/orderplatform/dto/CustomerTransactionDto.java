package com.mihaela.orderplatform.dto;

import com.mihaela.orderplatform.enums.Currency;
import com.mihaela.orderplatform.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTransactionDto {

    private Long id;

    private String customerId;

    private BigDecimal amount;

    private Currency currency;

    private TransactionStatus status;

    private Instant createdAt;

    private Instant updatedAt;
}