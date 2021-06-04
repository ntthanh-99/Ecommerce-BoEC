package com.tienthanh.domain.security;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.tienthanh.domain.Account;

@Entity
public class PasswordResetToken {
	private static final int EXPIRRATION = 60 * 24;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String token;

	@OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
	private Account account;

	private Date expiryDate;

	public PasswordResetToken() {
	}

	public PasswordResetToken(final String token, final Account account) {
		this.token = token;
		this.account = account;
		this.expiryDate = calculateExpiryDate(EXPIRRATION);
	}

	public Date calculateExpiryDate(final int expiryTimeinMinutes) {
		final Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());
		calendar.add(Calendar.MINUTE, expiryTimeinMinutes);
		return new Date(calendar.getTime().getTime());
	}

	public void updateToken(final String token) {
		this.token = token;
		this.expiryDate = calculateExpiryDate(EXPIRRATION);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public static int getExpirration() {
		return EXPIRRATION;
	}

	@Override
	public String toString() {
		return "PasswordResetToken [id=" + id + ", token=" + token + ", user=" + account + ", expiryDate=" + expiryDate
				+ "]";
	}
}
