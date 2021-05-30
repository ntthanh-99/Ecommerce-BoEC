package com.tienthanh.service;

import java.util.Set;

import com.tienthanh.domain.User;
import com.tienthanh.domain.UserBilling;
import com.tienthanh.domain.UserPayment;
import com.tienthanh.domain.UserShipping;
import com.tienthanh.domain.security.PasswordResetToken;
import com.tienthanh.domain.security.UserRole;

public interface UserService {
	PasswordResetToken getPasswordResetToken(final String token);

	void createPasswordResetToken(final User user, final String token);

	User findByUsername(String username);

	User findByEmail(String email);

	User createUser(User user, Set<UserRole> userRoles);

	User save(User user);
	
	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	
	void updateUserShipping(UserShipping userShipping, User user);
	
	void setUserDefaultPayment(Long id, User user);

	void setUserDefaultShipping(Long defaultShippingId, User user);
}
