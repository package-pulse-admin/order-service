package com.mihaela.orderplatform.repository;

import com.mihaela.orderplatform.domain.CustomerOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrders, Long> {

    Page<CustomerOrders> findAll(Pageable pageable);

    List<CustomerOrders> findByCustomerId(String customerId);


    List<CustomerOrders> findByStatus(String status);


    List<CustomerOrders> findByCustomerIdAndStatus(String customerId, String status);

}
