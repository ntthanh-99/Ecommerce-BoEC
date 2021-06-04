package com.tienthanh.repository;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.security.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	PasswordResetToken findByToken(final String token);

	PasswordResetToken findByAccount(Account account);

	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);

	@Modifying
	@Query(value = "DELETE FROM PasswordResetToken t WHERE t.expiryDate < ?1")
	void deleteAllExpiredSince(Date now);
}
