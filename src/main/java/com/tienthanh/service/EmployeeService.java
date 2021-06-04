package com.tienthanh.service;

import java.util.List;
import java.util.Set;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.employee.Employee;
import com.tienthanh.domain.security.AccountRole;;

public interface EmployeeService {
	Employee createEmployee(Employee employee, Set<AccountRole> accountRoles);

	Employee findByAccount(Account account);
}
