package com.tienthanh;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tienthanh.config.SecurityConfig;
import com.tienthanh.domain.Account;
import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.employee.Employee;
import com.tienthanh.domain.security.AccountRole;
import com.tienthanh.domain.security.Role;
import com.tienthanh.service.CustomerService;
import com.tienthanh.service.EmployeeService;
import com.tienthanh.service.impl.FormatDateImpl;
import com.tienthanh.utility.SecurityUtility;

@SpringBootApplication
public class ECommerceBookStoreApplication implements CommandLineRunner {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private FormatDateImpl formateDate;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceBookStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Customer customer = new Customer();
		Account account = new Account();
		account.setUsername("user");
		account.setPassword(SecurityConfig.passwordEncoder().encode("user"));
		account.setPosition("user");
		customer.setAccount(account);
		customer.setCreateDate(formateDate.convertLocalDateTimeToDate(LocalDateTime.now()));

		Role role = new Role();
		role.setId(1);
		role.setName("ROLE-USER");

		Set<AccountRole> accountRoles = new HashSet<AccountRole>();
		accountRoles.add(new AccountRole(role, account));

		customerService.createCustomer(customer, accountRoles);

	}

}
