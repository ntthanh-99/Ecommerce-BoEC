package com.tienthanh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.employee.Employee;
import com.tienthanh.repository.AccountRepository;
import com.tienthanh.repository.EmployeeRepository;

@Service
public class UserSecurityService implements UserDetailsService{
	@Autowired
	private AccountRepository accountRepository;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Account account = accountRepository.findByUsername(username);
		if (account == null) {
			throw new UsernameNotFoundException("Employee not Found!");
		}
		return account;
	}
	
}
