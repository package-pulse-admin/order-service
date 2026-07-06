package com.mihaela.orderplatform.service;

import com.mihaela.orderplatform.api.exception.TransactionNotFoundException;
import com.mihaela.orderplatform.domain.CustomerOrders;
import com.mihaela.orderplatform.dto.CustomerOrderDto;
import com.mihaela.orderplatform.enums.Currency;
import com.mihaela.orderplatform.enums.TransactionStatus;
import com.mihaela.orderplatform.mapper.CustomerOrderMapper;
import com.mihaela.orderplatform.repository.CustomerOrderRepository;
import com.mihaela.orderplatform.service.metrics.CustomMetricsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerOrderService {

    private static final Sort DEFAULT_SORT = Sort.by("id").ascending();

    private final CustomerOrderRepository repository;
    private final CustomerOrderMapper mapper;
    private final CustomMetricsService metricsService;

    public Page<CustomerOrderDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, DEFAULT_SORT);
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public CustomerOrderDto findById(Long id) {
        CustomerOrders transaction =
                repository.findById(id)
                        .orElseThrow(() -> new TransactionNotFoundException(id));
        return mapper.toDto(transaction);
    }

    @Transactional
    public CustomerOrderDto save(CustomerOrderDto dto) {
        dto.setStatus(TransactionStatus.CREATED);
        dto.setCurrency(Currency.fromValue(dto.getCurrency().name()));

        CustomerOrders entity = mapper.toEntity(dto);
        CustomerOrders saved = repository.save(entity);

        metricsService.incrementOrdersMetric("SUCCESS", TransactionStatus.CREATED);
        return mapper.toDto(saved);
    }

    public List<CustomerOrderDto> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<CustomerOrderDto> findByStatus(TransactionStatus status) {
        return repository.findByStatus(status.name())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<CustomerOrderDto> findByCustomerIdAndStatus(String customerId, TransactionStatus status) {
        return repository.findByCustomerIdAndStatus(customerId, status.name())
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
