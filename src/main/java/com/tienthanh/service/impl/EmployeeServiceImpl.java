package com.tienthanh.service.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.config.SecurityConfig;
import com.tienthanh.domain.Account;
import com.tienthanh.domain.employee.Employee;
import com.tienthanh.domain.security.AccountRole;
import com.tienthanh.repository.AccountRepository;
import com.tienthanh.repository.EmployeeRepository;
import com.tienthanh.repository.RoleRepository;
import com.tienthanh.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	//private static final Logger LOG = (Logger) LoggerFactory.logger(EmployeeService.class);
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private AccountRepository accountRepository;

	@Override
	public Employee createEmployee(Employee employee, Set<AccountRole> accountRoles) {
		// TODO Auto-generated method stub
		Employee localEmployee = employeeRepository
				.findByAccount(accountRepository.findByUsername(employee.getAccount().getUsername()));
		if(localEmployee!=null) {
			System.out.println("Employee is Exist! Nothing work be done!");
		}
		else {
			for (AccountRole accountRole : accountRoles) {
				roleRepository.save(accountRole.getRole());
			}
			Account account = employee.getAccount();
			account.getAccountRoles().addAll(accountRoles);
			localEmployee= employeeRepository.save(employee);
		}
		return localEmployee;
	}

	@Override
	public Employee findByAccount(Account account) {
		// TODO Auto-generated method stub
		return employeeRepository.findByAccount(account);
	}
	
}
