package com.tienthanh.service;

import java.util.List;

import com.tienthanh.domain.oder.SaleOff;
import com.tienthanh.domain.oder.SaleOffForProduct;
import com.tienthanh.domain.oder.SaleOffForTotal;

public interface SaleOffService {
	List<SaleOff> findAll();

	SaleOff findSaleOffById(Long id);

	SaleOffForProduct findSaleOffForProductBySaleOff(SaleOff saleOff);

	SaleOffForTotal findSaleOffForTotalBySaleOff(SaleOff saleOff);

	SaleOffForProduct saveSaleOffForProduct(SaleOffForProduct saleOffForProduct);

	SaleOffForTotal saveSaleOffForTotal(SaleOffForTotal saleOffForTotal);

	void deleteSaleOffForProductById(Long id);

	void deleteSaleOffForTotalById(Long id);
}
