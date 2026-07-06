package com.mihaela.orderplatform.service;

import com.mihaela.orderplatform.api.exception.TransactionNotFoundException;
import com.mihaela.orderplatform.domain.CustomerOrders;
import com.mihaela.orderplatform.dto.CustomerOrderDto;
import com.mihaela.orderplatform.enums.Currency;
import com.mihaela.orderplatform.enums.TransactionStatus;
import com.mihaela.orderplatform.mapper.CustomerOrderMapper;
import com.mihaela.orderplatform.repository.CustomerOrderRepository;
import com.mihaela.orderplatform.service.metrics.CustomMetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerOrderServiceTest {

    @Mock
    private CustomerOrderRepository repository;

    @Mock
    private CustomerOrderMapper mapper;

    @Mock
    private CustomMetricsService metricsService;

    @InjectMocks
    private CustomerOrderService service;

    private CustomerOrders entity;
    private CustomerOrderDto dto;

    @BeforeEach
    void setUp() {
        entity = new CustomerOrders();
        entity.setId(1L);

        dto = new CustomerOrderDto();
        dto.setCurrency(Currency.EUR);
    }

    @ParameterizedTest
    @EnumSource(TransactionStatus.class)
    void shouldFindByStatus(TransactionStatus status) {
        CustomerOrders entity = new CustomerOrders();
        CustomerOrderDto dto = new CustomerOrderDto();

        when(repository.findByStatus(status.name())).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<CustomerOrderDto> result = service.findByStatus(status);

        assertEquals(1, result.size());
        verify(repository).findByStatus(status.name());
        verify(mapper).toDto(entity);
    }

    @ParameterizedTest
    @MethodSource("customerStatusProvider")
    void shouldFindByCustomerIdAndStatus(String customerId, TransactionStatus status) {
        CustomerOrders entity = new CustomerOrders();
        CustomerOrderDto dto = new CustomerOrderDto();

        when(repository.findByCustomerIdAndStatus(customerId, status.name()))
                .thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<CustomerOrderDto> result =
                service.findByCustomerIdAndStatus(customerId, status);

        assertEquals(1, result.size());

        verify(repository).findByCustomerIdAndStatus(customerId, status.name());
        verify(mapper).toDto(entity);
    }

    @ParameterizedTest
    @ValueSource(strings = {"cust1", "cust2", "cust3"})
    void shouldFindByCustomerId(String customerId) {
        CustomerOrders entity = new CustomerOrders();
        CustomerOrderDto dto = new CustomerOrderDto();

        when(repository.findByCustomerId(customerId)).thenReturn(List.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        List<CustomerOrderDto> result = service.findByCustomerId(customerId);

        assertEquals(1, result.size());
        verify(repository).findByCustomerId(customerId);
    }

    @Test
    void shouldReturnPagedOrders() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<CustomerOrders> page = new PageImpl<>(List.of(entity));

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toDto(entity)).thenReturn(dto);

        Page<CustomerOrderDto> result = service.findAll(0, 10);

        assertEquals(1, result.getContent().size());
        verify(repository).findAll(pageable);
        verify(mapper).toDto(entity);
    }

    @Test
    void shouldReturnOrderById() {
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toDto(entity)).thenReturn(dto);

        CustomerOrderDto result = service.findById(1L);

        assertNotNull(result);
        verify(repository).findById(1L);
        verify(mapper).toDto(entity);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class,
                () -> service.findById(1L));

        verify(repository).findById(1L);
        verifyNoInteractions(mapper);
    }

    @Test
    void shouldSaveOrderAndUpdateMetrics() {
        CustomerOrderDto inputDto = new CustomerOrderDto();
        inputDto.setCurrency(Currency.EUR);

        CustomerOrders savedEntity = new CustomerOrders();
        savedEntity.setId(1L);

        CustomerOrderDto mappedBackDto = new CustomerOrderDto();

        when(mapper.toEntity(any(CustomerOrderDto.class))).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);
        when(mapper.toDto(savedEntity)).thenReturn(mappedBackDto);

        CustomerOrderDto result = service.save(inputDto);

        assertNotNull(result);

        assertEquals(TransactionStatus.CREATED, inputDto.getStatus());
        assertEquals(Currency.EUR, inputDto.getCurrency());

        verify(repository).save(entity);
        verify(metricsService).incrementOrdersMetric("SUCCESS", TransactionStatus.CREATED);
    }

    private static Stream<Arguments> customerStatusProvider() {
        return Stream.of(
                Arguments.of("cust1", TransactionStatus.CREATED),
                Arguments.of("cust2", TransactionStatus.COMPLETED),
                Arguments.of("cust3", TransactionStatus.PAYMENT_FAILED)
        );
    }
}
