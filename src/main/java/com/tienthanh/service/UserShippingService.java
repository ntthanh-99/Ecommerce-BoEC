package com.tienthanh.service;

import com.tienthanh.domain.UserShipping;

public interface UserShippingService {
	UserShipping findById(Long id);

	void removeById(Long userShippingId);
}
