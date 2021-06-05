package com.tienthanh.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.customer.CustomerPayment;
import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.security.AccountRole;
import com.tienthanh.domain.security.PasswordResetToken;
import com.tienthanh.repository.AccountRepository;
import com.tienthanh.repository.CustomerPaymentRepository;
import com.tienthanh.repository.CustomerRepository;
import com.tienthanh.repository.CustomerShippingRepository;
import com.tienthanh.repository.PasswordResetTokenRepository;
import com.tienthanh.repository.RoleRepository;
import com.tienthanh.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private CustomerPaymentRepository customerPaymentRepository;

	@Autowired
	private CustomerShippingRepository customerShippingRepository;

	@Override
	public Customer createCustomer(Customer customer, Set<AccountRole> accountRoles) {
		// TODO Auto-generated method stub
		Customer localCustomer = customerRepository
				.findByAccount(accountRepository.findByUsername(customer.getAccount().getUsername()));
		if (localCustomer != null) {
			System.out.println("Customer is Exist! Nothing work be done!");
		} else {
			for (AccountRole accountRole : accountRoles) {
				roleRepository.save(accountRole.getRole());
			}
			Account account = customer.getAccount();
			account.getAccountRoles().addAll(accountRoles);
			localCustomer = customerRepository.save(customer);
		}
		return localCustomer;
	}

	@Override
	public Customer findByAccount(Account account) {
		// TODO Auto-generated method stub
		return customerRepository.findByAccount(account);
	}

	@Override
	public void updateCustomerPayment(CustomerPayment customerPayment, Customer customer) {
		// TODO Auto-generated method stub
		customerPayment.setCustomer(customer);
		customerPayment.setDefaultPayment(true);
		customer.getCustomerPaymentList().add(customerPayment);
		customerRepository.save(customer);
	}

	@Override
	public CustomerPayment findCustomerPaymentById(Long id) {
		// TODO Auto-generated method stub
		return customerPaymentRepository.findById(id).get();
	}

	@Override
	public void removeCustomerPaymentById(Long id) {
		// TODO Auto-generated method stub
		customerPaymentRepository.deleteById(id);
	}

	@Override
	public void setCustomerDefaultPayment(Long id, Customer customer) {
		// TODO Auto-generated method stub
		List<CustomerPayment> customerPaymentList = (List<CustomerPayment>) customerPaymentRepository.findAll();
		for (CustomerPayment customerPayment : customerPaymentList) {
			if (customerPayment.getId() == id) {
				customerPayment.setDefaultPayment(true);
				customerPaymentRepository.save(customerPayment);
			} else {
				customerPayment.setDefaultPayment(false);
				customerPaymentRepository.save(customerPayment);
			}
		}
	}

	@Override
	public void updateCustomerShipping(CustomerShipping customerShipping, Customer customer) {
		// TODO Auto-generated method stub
		customerShipping.setCustomer(customer);
		customerShipping.setDefaultShipping(true);
		customer.getCustomerShippingList().add(customerShipping);
		customerRepository.save(customer);
	}

	@Override
	public CustomerShipping findCustomerShippingById(Long id) {
		// TODO Auto-generated method stub
		return customerShippingRepository.findById(id).get();
	}

	@Override
	public void removeCustomerShippingById(Long id) {
		// TODO Auto-generated method stub
		customerShippingRepository.deleteById(id);
	}

	@Override
	public void setCustomerDefaultShipping(Long id, Customer customer) {
		// TODO Auto-generated method stub
		List<CustomerShipping> customerShippingList = (List<CustomerShipping>) customerShippingRepository.findAll();
		for (CustomerShipping customerShipping : customerShippingList) {
			if (customerShipping.getId() == id) {
				customerShipping.setDefaultShipping(true);
				customerShippingRepository.save(customerShipping);
			} else {
				customerShipping.setDefaultShipping(false);
				customerShippingRepository.save(customerShipping);
			}
		}
	}

	@Override
	public Customer saveCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return customerRepository.save(customer);
	}
}
