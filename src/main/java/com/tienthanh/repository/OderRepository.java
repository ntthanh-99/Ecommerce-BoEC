package com.tienthanh.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.oder.Oder;

public interface OderRepository extends CrudRepository<Oder, Long> {
	List<Oder> findByCustomer(Customer customer);
}
