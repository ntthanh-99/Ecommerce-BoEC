package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.product.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {

}
