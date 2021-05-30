package com.tienthanh.utility;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtility {
	private static final String SALT = "salt";

	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}

	@Bean
	public static String randomPassword() {
		String SALTCHARS = "ABCDEFGHIKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rd = new Random();

		while (salt.length() < 18) {
			int index = (int) (rd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltString = salt.toString();
		return saltString;
	}
}
