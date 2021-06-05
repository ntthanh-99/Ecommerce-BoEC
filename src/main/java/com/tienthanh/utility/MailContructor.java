package com.tienthanh.utility;

import java.util.Locale;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.customer.CustomerPayment;
import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.oder.Oder;

@Component
public class MailContructor {
	@Autowired
	private Environment env;

	@Autowired
	private TemplateEngine templateEngine;
	
	public SimpleMailMessage contructResetTokenEmail(String contextPath, Locale locale, String token, Account account,
			String password) {
		String url = contextPath + "/newUser?token=" + token;
		String message = "\n Vui lòng click vào đường dẫn phía trên để xác thực và thay đổi thông tin cá nhân. Mật khẩu của bạn là: \n"
				+ password;
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(account.getEmail());
		email.setSubject("BoEC - New User");
		email.setText(url + message);
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
	
	public MimeMessagePreparator constructOrderConfirmationEmail(Account account, CustomerShipping customerShipping,
			CustomerPayment customerPayment, Oder oder, Locale locale) {
		Context context = new Context();
		context.setVariable("order", oder);
		context.setVariable("user", account);
		context.setVariable("customerShipping", customerShipping);
		context.setVariable("customerPayment", customerPayment);
		context.setVariable("cartItemList", oder.getCartProductList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(account.getEmail());
				email.setSubject("Order Confirmation - " + oder.getId());
				email.setText(text, true);
				email.setFrom(new InternetAddress("nguyentienthanh20091108@gmail.com"));
			}
		};
		
		return messagePreparator;
	}

}
