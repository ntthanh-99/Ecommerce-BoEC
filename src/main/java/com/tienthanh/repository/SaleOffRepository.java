package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.oder.SaleOff;
import com.tienthanh.service.SaleOffService;

public interface SaleOffRepository extends CrudRepository<SaleOff, Long> {

}
