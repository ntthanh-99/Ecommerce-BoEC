package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.employee.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long>{
	Employee findByAccount(Account account);
}
