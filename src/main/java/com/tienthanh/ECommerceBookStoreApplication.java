package com.tienthanh;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tienthanh.domain.User;
import com.tienthanh.domain.security.Role;
import com.tienthanh.domain.security.UserRole;
import com.tienthanh.service.UserService;
import com.tienthanh.utility.SecurityUtility;

@SpringBootApplication
public class ECommerceBookStoreApplication implements CommandLineRunner {
	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(ECommerceBookStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		User user1 = new User();
		user1.setFirstName("Thanh");
		user1.setLastName("Nguyen Tien");
		user1.setUsername("r");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("tienthanh99hn@gmail.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));

		userService.createUser(user1, userRoles);

	}

}
