package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.oder.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long> {
	ShoppingCart findByCustomer(Customer customer);
}
