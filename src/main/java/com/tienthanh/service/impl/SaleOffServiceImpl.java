package com.tienthanh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.oder.SaleOff;
import com.tienthanh.domain.oder.SaleOffForProduct;
import com.tienthanh.domain.oder.SaleOffForTotal;
import com.tienthanh.repository.SaleOffForProductRepository;
import com.tienthanh.repository.SaleOffForTotalRepository;
import com.tienthanh.repository.SaleOffRepository;
import com.tienthanh.service.SaleOffService;

@Service
public class SaleOffServiceImpl implements SaleOffService {
	@Autowired
	private SaleOffRepository saleOffRepository;

	@Autowired
	private SaleOffForProductRepository saleOffForProductRepository;

	@Autowired
	private SaleOffForTotalRepository saleOffForTotalRepositoty;

	@Override
	public List<SaleOff> findAll() {
		// TODO Auto-generated method stub
		return (List<SaleOff>) saleOffRepository.findAll();
	}

	@Override
	public SaleOffForProduct saveSaleOffForProduct(SaleOffForProduct saleOffForProduct) {
		// TODO Auto-generated method stub
		return saleOffForProductRepository.save(saleOffForProduct);
	}

	@Override
	public SaleOffForTotal saveSaleOffForTotal(SaleOffForTotal saleOffForTotal) {
		// TODO Auto-generated method stub
		return saleOffForTotalRepositoty.save(saleOffForTotal);
	}

	@Override
	public SaleOff findSaleOffById(Long id) {
		// TODO Auto-generated method stub
		return saleOffRepository.findById(id).get();
	}

	@Override
	public SaleOffForProduct findSaleOffForProductBySaleOff(SaleOff saleOff) {
		// TODO Auto-generated method stub
		return saleOffForProductRepository.findBySaleOff(saleOff);
	}

	@Override
	public SaleOffForTotal findSaleOffForTotalBySaleOff(SaleOff saleOff) {
		// TODO Auto-generated method stub
		return saleOffForTotalRepositoty.findBySaleOff(saleOff);
	}

	@Override
	public void deleteSaleOffForProductById(Long id) {
		// TODO Auto-generated method stub
		saleOffForProductRepository.deleteById(id);
	}

	@Override
	public void deleteSaleOffForTotalById(Long id) {
		// TODO Auto-generated method stub
		saleOffForTotalRepositoty.deleteById(id);
	}

}
