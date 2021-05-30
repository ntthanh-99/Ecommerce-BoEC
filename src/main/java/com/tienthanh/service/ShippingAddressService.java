package com.tienthanh.service;

import com.tienthanh.domain.ShippingAddress;
import com.tienthanh.domain.UserShipping;

public interface ShippingAddressService {
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);
}
