package com.tienthanh.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.ShoppingCart;
import com.tienthanh.domain.User;
import com.tienthanh.domain.UserBilling;
import com.tienthanh.domain.UserPayment;
import com.tienthanh.domain.UserShipping;
import com.tienthanh.domain.security.PasswordResetToken;
import com.tienthanh.domain.security.UserRole;
import com.tienthanh.repository.PasswordResetTokenRepository;
import com.tienthanh.repository.RoleRepository;
import com.tienthanh.repository.UserPaymentRepository;
import com.tienthanh.repository.UserRepository;
import com.tienthanh.repository.UserShippingRepository;
import com.tienthanh.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserPaymentRepository userPaymentRepository;
	
	@Autowired
	UserShippingRepository userShippingRepository;

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		// TODO Auto-generated method stub
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetToken(User user, String token) {
		// TODO Auto-generated method stub
		final PasswordResetToken myToken = new PasswordResetToken(token, user);
		passwordResetTokenRepository.save(myToken);
		return;
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) {
		// TODO Auto-generated method stub
		User localUser = userRepository.findByUsername(user.getUsername());
		if (localUser != null) {
			LOG.info("User already Exist. Nothing will be done");
		} else {
			for (UserRole ur : userRoles) {
				roleRepository.save(ur.getRole());
			}
			user.getUserRoles().addAll(userRoles);
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			user.setUserShippingList(new ArrayList<UserShipping>());
			user.setUserPaymentList(new ArrayList<UserPayment>());
			localUser = userRepository.save(user);
		}
		return localUser;
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.save(user);
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		// TODO Auto-generated method stub
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
	}

	@Override
	public void setUserDefaultPayment(Long id, User user) {
		// TODO Auto-generated method stub
		List<UserPayment> userPaymentList = (List<UserPayment>) userPaymentRepository.findAll();
		for (UserPayment userPayment : userPaymentList) {
			if(userPayment.getId() == id) {
				userPayment.setDefaultPayment(true);
				userPaymentRepository.save(userPayment);
			}
			else {
				userPayment.setDefaultPayment(false);
				userPaymentRepository.save(userPayment);
			}
		}
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		// TODO Auto-generated method stub
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
	}

	@Override
	public void setUserDefaultShipping(Long defaultShippingId, User user) {
		// TODO Auto-generated method stub
		List<UserShipping> userShippingList = (List<UserShipping>) userShippingRepository.findAll();
		for (UserShipping userShipping : userShippingList) {
			if(userShipping.getId() == defaultShippingId) {
				userShipping.setUserShippingDefault(true);
				userShippingRepository.save(userShipping);
			}
			else {
				userShipping.setUserShippingDefault(false);
				userShippingRepository.save(userShipping);
			}
		}
	}

}
