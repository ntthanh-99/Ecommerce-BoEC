package com.tienthanh.service;

import java.util.Set;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.customer.CustomerPayment;
import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.security.AccountRole;
import com.tienthanh.domain.security.PasswordResetToken;

public interface CustomerService {
	Customer createCustomer(Customer customer, Set<AccountRole> accountRoles);

	Customer findByAccount(Account account);

	void updateCustomerPayment(CustomerPayment customerPayment, Customer customer);

	CustomerPayment findCustomerPaymentById(Long id);

	void removeCustomerPaymentById(Long id);

	void setCustomerDefaultPayment(Long id, Customer customer);

	void updateCustomerShipping(CustomerShipping customerShipping, Customer customer);

	CustomerShipping findCustomerShippingById(Long id);

	void removeCustomerShippingById(Long id);

	void setCustomerDefaultShipping(Long id, Customer customer);
}
