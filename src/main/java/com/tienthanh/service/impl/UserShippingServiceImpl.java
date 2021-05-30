package com.tienthanh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.UserShipping;
import com.tienthanh.repository.UserShippingRepository;
import com.tienthanh.service.UserShippingService;

@Service
public class UserShippingServiceImpl implements UserShippingService{
	@Autowired
	UserShippingRepository userShippingRepository;
	
	@Override
	public UserShipping findById(Long id) {
		// TODO Auto-generated method stub
		return userShippingRepository.findById(id).get();
	}

	@Override
	public void removeById(Long userShippingId) {
		// TODO Auto-generated method stub
		userShippingRepository.deleteById(userShippingId);
		return;
	}
	
}
