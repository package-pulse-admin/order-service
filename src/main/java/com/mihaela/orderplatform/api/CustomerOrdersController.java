package com.mihaela.orderplatform.api;

import com.mihaela.orderplatform.dto.CustomerOrderDto;
import com.mihaela.orderplatform.enums.TransactionStatus;
import com.mihaela.orderplatform.service.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/customer-transactions")
@RequiredArgsConstructor
public class CustomerOrdersController {

    private final CustomerOrderService service;

    @GetMapping
    public ResponseEntity<Page<CustomerOrderDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.debug("Request to fetch customer transactions. page={}, size={}", page, size);
        return ResponseEntity.ok(service.findAll(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerOrderDto> findById(@PathVariable Long id) {
        log.debug("Request to fetch customer transaction for id {}", id);
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<CustomerOrderDto> save(@RequestBody CustomerOrderDto dto) {
        log.debug("Request to save new transaction for customer {}", dto.getCustomerId());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }


    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CustomerOrderDto>> findByCustomerId(@PathVariable String customerId) {

        return ResponseEntity.ok(service.findByCustomerId(customerId));
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<CustomerOrderDto>> findByStatus(
            @PathVariable TransactionStatus status) {

        return ResponseEntity.ok(
                service.findByStatus(status)
        );
    }


    @GetMapping("/search")
    public ResponseEntity<List<CustomerOrderDto>> findByCustomerIdAndStatus(
            @RequestParam String customerId,
            @RequestParam TransactionStatus status) {

        return ResponseEntity.ok(
                service.findByCustomerIdAndStatus(
                        customerId,
                        status
                )
        );
    }
}