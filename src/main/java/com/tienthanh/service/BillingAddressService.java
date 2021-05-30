package com.tienthanh.service;

import com.tienthanh.domain.BillingAddress;
import com.tienthanh.domain.UserBilling;

public interface BillingAddressService {
	BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress);
}	
