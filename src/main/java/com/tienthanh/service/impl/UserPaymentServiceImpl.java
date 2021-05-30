package com.tienthanh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.UserPayment;
import com.tienthanh.repository.UserPaymentRepository;
import com.tienthanh.service.UserPaymentService;

@Service
public class UserPaymentServiceImpl implements UserPaymentService{
	@Autowired
	UserPaymentRepository userPaymentRepository;
	@Override
	public UserPayment findById(Long id) {
		// TODO Auto-generated method stub
		return userPaymentRepository.findById(id).get();
	}
	@Override
	public void removeById(Long id) {
		// TODO Auto-generated method stub
		userPaymentRepository.deleteById(id);
		return;
	}
	
}
