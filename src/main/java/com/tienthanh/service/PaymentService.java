package com.tienthanh.service;

import com.tienthanh.domain.Payment;
import com.tienthanh.domain.UserPayment;

public interface PaymentService {
	Payment setByUserPayment(UserPayment userPayment, Payment payment);
}
