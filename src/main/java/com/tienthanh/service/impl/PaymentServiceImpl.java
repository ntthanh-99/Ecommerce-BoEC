package com.tienthanh.service.impl;

import org.springframework.stereotype.Service;

import com.tienthanh.domain.Payment;
import com.tienthanh.domain.UserPayment;
import com.tienthanh.service.PaymentService;
@Service
public class PaymentServiceImpl implements PaymentService{

	@Override
	public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
		// TODO Auto-generated method stub
		payment.setType(userPayment.getType());
		payment.setHolderName(userPayment.getHolderName());
		payment.setCardNumber(userPayment.getCardNumber());
		payment.setExpiryMonth(userPayment.getExpiryMonth());
		payment.setExpiryYear(userPayment.getExpiryYear());
		payment.setCvc(userPayment.getCvc());
		
		return payment;
	}
	
}
