package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.customer.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
	Customer findByAccount(Account account);
}
