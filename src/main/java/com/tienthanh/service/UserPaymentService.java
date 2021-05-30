package com.tienthanh.service;

import com.tienthanh.domain.UserPayment;

public interface UserPaymentService {
	 UserPayment findById(Long id);
	 
	 void removeById(Long id);
}