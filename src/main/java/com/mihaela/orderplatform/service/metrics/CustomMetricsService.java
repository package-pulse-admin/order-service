package com.mihaela.orderplatform.service.metrics;

import com.mihaela.orderplatform.enums.TransactionStatus;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Component
public class CustomMetricsService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> orderCounters = new ConcurrentHashMap<>();

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void incrementOrdersMetric(String status, TransactionStatus transactionStatus) {
        orderCounter(status, transactionStatus).increment();
    }

    private Counter orderCounter(String status, TransactionStatus transactionStatus) {
        String key = status + "_" + transactionStatus.name();
        return orderCounters.computeIfAbsent(key, k ->
                Counter.builder("orders_total")
                        .description("Total orders by status")
                        .tag("status", status)
                        .tag("transactionStatus", transactionStatus.name())
                        .register(meterRegistry)
        );
    }
}
