package com.mihaela.orderplatform.domain;

import com.mihaela.orderplatform.enums.Currency;
import com.mihaela.orderplatform.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record PublishedOrderEvent(
        String orderId,
        TransactionStatus status,
        BigDecimal amount,
        Currency currency,
        Instant createdAt
) {
}
