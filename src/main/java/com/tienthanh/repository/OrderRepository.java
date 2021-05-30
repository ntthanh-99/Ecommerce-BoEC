package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
