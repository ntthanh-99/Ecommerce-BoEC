package com.tienthanh.service;

import com.tienthanh.domain.BillingAddress;
import com.tienthanh.domain.Order;
import com.tienthanh.domain.Payment;
import com.tienthanh.domain.ShippingAddress;
import com.tienthanh.domain.ShoppingCart;
import com.tienthanh.domain.User;

public interface OrderService {
	Order findById(Long id);
	
	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress,
			          BillingAddress billingAddress,Payment payment,
			          String shippingMethod, User user);
}
