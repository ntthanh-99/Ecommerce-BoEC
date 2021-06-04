package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.oder.SaleOff;
import com.tienthanh.domain.oder.SaleOffForProduct;

public interface SaleOffForProductRepository extends CrudRepository<SaleOffForProduct, Long> {
	SaleOffForProduct findBySaleOff(SaleOff saleOff);
}
