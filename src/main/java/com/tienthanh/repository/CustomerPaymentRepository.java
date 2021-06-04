package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.customer.CustomerPayment;

public interface CustomerPaymentRepository extends CrudRepository<CustomerPayment, Long> {

}
