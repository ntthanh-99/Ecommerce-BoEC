package com.tienthanh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.security.PasswordResetToken;
import com.tienthanh.repository.AccountRepository;
import com.tienthanh.repository.PasswordResetTokenRepository;
import com.tienthanh.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		// TODO Auto-generated method stub
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public Account findByEmail(String email) {
		// TODO Auto-generated method stub
		return accountRepository.findByEmail(email);
	}

	@Override
	public Account save(Account account) {
		// TODO Auto-generated method stub
		return accountRepository.save(account);
	}

	@Override
	public void createPasswordResetToken(Account account, String token) {
		// TODO Auto-generated method stub
		final PasswordResetToken myToken = new PasswordResetToken(token, account);
		passwordResetTokenRepository.save(myToken);
		return;
	}

	@Override
	public Account findByUsername(String username) {
		// TODO Auto-generated method stub
		return accountRepository.findByUsername(username);
	}

}
