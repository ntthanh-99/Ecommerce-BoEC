package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {
	Account findByUsername(String username);

	Account findByEmail(String email);
}
