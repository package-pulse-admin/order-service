package com.mihaela.orderplatform.repository;

import com.mihaela.orderplatform.domain.CustomerTransactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransactions, Long> {

    Page<CustomerTransactions> findAll(Pageable pageable);

    List<CustomerTransactions> findByCustomerId(String customerId);


    List<CustomerTransactions> findByStatus(String status);


    List<CustomerTransactions> findByCustomerIdAndStatus(String customerId, String status);

}
