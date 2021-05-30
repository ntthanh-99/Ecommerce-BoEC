package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long>{
 
}
