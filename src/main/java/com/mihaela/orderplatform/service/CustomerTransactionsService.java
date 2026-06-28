package com.mihaela.orderplatform.service;

import com.mihaela.orderplatform.api.exception.TransactionNotFoundException;
import com.mihaela.orderplatform.domain.CustomerTransactions;
import com.mihaela.orderplatform.dto.CustomerTransactionDto;
import com.mihaela.orderplatform.enums.Currency;
import com.mihaela.orderplatform.enums.TransactionStatus;
import com.mihaela.orderplatform.mapper.CustomerTransactionMapper;
import com.mihaela.orderplatform.repository.CustomerTransactionRepository;
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
public class CustomerTransactionsService {

    private static final Sort DEFAULT_SORT = Sort.by("id").ascending();

    private final CustomerTransactionRepository repository;
    private final CustomerTransactionMapper mapper;
    private final CustomMetricsService metricsService;

    public Page<CustomerTransactionDto> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, DEFAULT_SORT);
        return repository.findAll(pageable).map(mapper::toDto);
    }

    public CustomerTransactionDto findById(Long id) {
        CustomerTransactions transaction =
                repository.findById(id)
                        .orElseThrow(() -> new TransactionNotFoundException(id));
        return mapper.toDto(transaction);
    }

    @Transactional
    public CustomerTransactionDto save(CustomerTransactionDto dto) {
        dto.setStatus(TransactionStatus.CREATED);
        dto.setCurrency(Currency.fromValue(dto.getCustomerId()));

        CustomerTransactions entity = mapper.toEntity(dto);

        CustomerTransactions saved = repository.save(entity);

        metricsService.incrementOrdersMetric("SUCCESS", TransactionStatus.CREATED);
        return mapper.toDto(saved);
    }

    public List<CustomerTransactionDto> findByCustomerId(String customerId) {
        return repository.findByCustomerId(customerId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<CustomerTransactionDto> findByStatus(TransactionStatus status) {
        return repository.findByStatus(status.name())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<CustomerTransactionDto> findByCustomerIdAndStatus(String customerId, TransactionStatus status) {
        return repository.findByCustomerIdAndStatus(customerId, status.name())
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}
