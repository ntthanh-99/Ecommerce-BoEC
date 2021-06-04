package com.tienthanh.config;

import java.security.SecureRandom;

import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tienthanh.service.UserSecurityService;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	//@Autowired
	//private Environment env;
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	private final static String SALT= "salt";
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
	}
	
	public final String[] PUBLIC_MATCHES = { "/css/**", "/js/**", "/image/**", "/", "/newUser", "/forgetPassword",
			"/login", "/fonts/**", "/book", "/electronic", "/clothes", "/bookDetail/**", "/hours", "/faq",
			"/searchByCategory",
			"/searchBook" };
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers(PUBLIC_MATCHES).permitAll().anyRequest().authenticated();
		http.csrf().disable().cors().disable()
			.formLogin().failureUrl("/login/?error").defaultSuccessUrl("/").loginPage("/login").permitAll()
			.and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
			.and()
			.rememberMe();
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder atmb) throws Exception {
		atmb.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
	}
}
