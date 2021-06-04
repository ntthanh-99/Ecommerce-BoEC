package com.tienthanh.service;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.security.PasswordResetToken;

public interface AccountService {
	PasswordResetToken getPasswordResetToken(String token);

	void createPasswordResetToken(Account account, String token);

	Account findByEmail(String email);

	Account save(Account account);

	Account findByUsername(String username);
}
