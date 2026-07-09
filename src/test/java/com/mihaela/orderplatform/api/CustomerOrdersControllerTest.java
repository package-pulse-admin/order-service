package com.mihaela.orderplatform.api;

import com.mihaela.orderplatform.dto.CustomerOrderDto;
import com.mihaela.orderplatform.enums.TransactionStatus;
import com.mihaela.orderplatform.service.CustomerOrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerOrdersControllerTest {

    @Mock
    private CustomerOrderService service;

    @InjectMocks
    private CustomerOrdersController controller;

    @Test
    void shouldReturnById() {
        CustomerOrderDto dto = new CustomerOrderDto();

        when(service.findById(1L)).thenReturn(dto);

        ResponseEntity<CustomerOrderDto> response = controller.findById(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(dto, response.getBody());

        verify(service).findById(1L);
    }

    @Test
    void shouldReturnByStatus() {
        CustomerOrderDto dto = new CustomerOrderDto();

        when(service.findByStatus(TransactionStatus.CREATED))
                .thenReturn(List.of(dto));

        ResponseEntity<List<CustomerOrderDto>> response =
                controller.findByStatus(TransactionStatus.CREATED);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());

        verify(service).findByStatus(TransactionStatus.CREATED);
    }

    @Test
    void shouldReturnByCustomerIdAndStatus() {
        CustomerOrderDto dto = new CustomerOrderDto();

        when(service.findByCustomerIdAndStatus("cust1", TransactionStatus.CREATED))
                .thenReturn(List.of(dto));

        ResponseEntity<List<CustomerOrderDto>> response =
                controller.findByCustomerIdAndStatus("cust1", TransactionStatus.CREATED);

        assertEquals(200, response.getStatusCode().value());

        verify(service).findByCustomerIdAndStatus("cust1", TransactionStatus.CREATED);
    }
}