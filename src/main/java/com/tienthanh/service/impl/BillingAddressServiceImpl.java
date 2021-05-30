package com.tienthanh.service.impl;

import org.springframework.stereotype.Service;

import com.tienthanh.domain.BillingAddress;
import com.tienthanh.domain.UserBilling;
import com.tienthanh.service.BillingAddressService;

@Service
public class BillingAddressServiceImpl implements BillingAddressService{

	@Override
	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		// TODO Auto-generated method stub
		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		billingAddress.setBillingAddressState(userBilling.getUserBillingState());
		billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
		billingAddress.setBillingAddressZipcode(userBilling.getUserBillingZipcode());
		
		return billingAddress;
	}
	
}
