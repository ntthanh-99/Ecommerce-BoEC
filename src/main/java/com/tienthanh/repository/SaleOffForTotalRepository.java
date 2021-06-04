package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.oder.SaleOff;
import com.tienthanh.domain.oder.SaleOffForTotal;

public interface SaleOffForTotalRepository extends CrudRepository<SaleOffForTotal, Long> {
	SaleOffForTotal findBySaleOff(SaleOff saleOff);

}
