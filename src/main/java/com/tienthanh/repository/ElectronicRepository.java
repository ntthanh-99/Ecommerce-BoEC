package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.product.Electronic;
import com.tienthanh.domain.product.Product;

public interface ElectronicRepository extends CrudRepository<Electronic, Long> {
	Electronic findByProduct(Product product);

}
