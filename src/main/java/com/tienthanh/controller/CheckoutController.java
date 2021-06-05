package com.tienthanh.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.customer.CustomerPayment;
import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.customer.Payment;
import com.tienthanh.domain.oder.CartProduct;
import com.tienthanh.domain.oder.Oder;
import com.tienthanh.domain.oder.ShoppingCart;
import com.tienthanh.repository.AccountRepository;
import com.tienthanh.service.CustomerService;
import com.tienthanh.service.OderService;
import com.tienthanh.utility.MailContructor;
import com.tienthanh.utility.USConstants;

@Controller
public class CheckoutController {
	private CustomerShipping customerShipping = new CustomerShipping();
	private CustomerPayment customerPayment = new CustomerPayment();
	private Payment payment = new Payment();


	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private OderService oderService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailContructor mailContructor;

	@RequestMapping("/checkout")
	public String checkout(@RequestParam("id") Long cartId,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField, Model model,
			Principal principal) {
		customerPayment.setPayment(payment);
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountRepository.findByUsername(username));

		if (cartId != customer.getShopingCart().getId()) {
			return "badRequestPage";
		}

		List<CartProduct> cartProductList = oderService.findByShoppingCart(customer.getShopingCart());

		if (cartProductList.size() == 0) {
			model.addAttribute("emptyCart", true);
			return "forward:/shoppingCart/cart";
		}

		for (CartProduct cartItem : cartProductList) {
			if (cartItem.getProduct().getQuanlity() < cartItem.getQuanlity()) {
				model.addAttribute("notEnoughStock", true);
				return "forward:/shoppingCart/cart";
			}
		}

		List<CustomerShipping> userShippingList = customer.getCustomerShippingList();
		List<CustomerPayment> userPaymentList = customer.getCustomerPaymentList();

		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);

		if (userPaymentList.size() == 0) {
			model.addAttribute("emptyPaymentList", true);
		} else {
			model.addAttribute("emptyPaymentList", false);
		}

		if (userShippingList.size() == 0) {
			model.addAttribute("emptyShippingList", true);
		} else {
			model.addAttribute("emptyShippingList", false);
		}

		ShoppingCart shoppingCart = customer.getShopingCart();

//		for (CustomerShipping userShipping : userShippingList) {
//			if (userShipping.isDefaultShipping()) {
//				customerService.setByUserShipping);
//			}
//		}

//		for (UserPayment userPayment : userPaymentList) {
//			if (userPayment.isDefaultPayment()) {
//				paymentService.setByUserPayment(userPayment, payment);
//				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
//			}
//		}
		double total = 0;
		for (CartProduct cartProduct : cartProductList) {
			total += cartProduct.getQuanlity() * cartProduct.getProduct().getPrice();
		}
		model.addAttribute("total", total);
		model.addAttribute("shippingAddress", customerShipping);
		model.addAttribute("payment", customerPayment);

		model.addAttribute("cartItemList", cartProductList);
		model.addAttribute("shoppingCart", customer.getShopingCart());

		model.addAttribute("classActiveShipping", true);


		if (missingRequiredField) {
			model.addAttribute("missingRequiredField", true);
		}

		return "checkout";
	}

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String checkoutPost(@ModelAttribute("shippingAddress") CustomerShipping customerShipping,
			@ModelAttribute("payment") CustomerPayment customerPayment, Principal principal, Model model) {

		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountRepository.findByUsername(username));
		ShoppingCart shoppingCart = customer.getShopingCart();

		List<CartProduct> cartItemList = oderService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList", cartItemList);

		if (customerShipping.getAddress().getNumber().isEmpty() || customerShipping.getAddress().getStreet().isEmpty()
				|| customerShipping.getAddress().getDistrict().isEmpty()
				|| customerShipping.getAddress().getCity().isEmpty())

			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";

		Oder order = oderService.createOrder(shoppingCart, customerShipping, customerPayment, customer);

		mailSender.send(mailContructor.constructOrderConfirmationEmail(customer.getAccount(), customerShipping,
				customerPayment, order, Locale.ENGLISH));

		oderService.clearShoppingCart(shoppingCart);

		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		estimatedDeliveryDate = today.plusDays(2);
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);

		return "orderSubmittedPage";
	}

	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId, Principal principal,
			Model model) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountRepository.findByUsername(username));
		customerShipping = customerService.findCustomerShippingById(userShippingId);

		if (customerShipping.getCustomer().getAccount().getId() != customer.getAccount().getId()) {
			return "badRequestPage";
		} else {

			List<CartProduct> cartProductList = oderService.findByShoppingCart(customer.getShopingCart());

			double total = 0;
			for (CartProduct cartProduct : cartProductList) {
				total += cartProduct.getQuanlity() * cartProduct.getProduct().getPrice();
			}
			model.addAttribute("total", total);
			model.addAttribute("shippingAddress", customerShipping);
			model.addAttribute("payment", customerPayment);

			model.addAttribute("cartItemList", cartProductList);
			model.addAttribute("shoppingCart", customer.getShopingCart());

			model.addAttribute("classActiveShipping", true);

			List<CustomerShipping> userShippingList = customer.getCustomerShippingList();
			List<CustomerPayment> userPaymentList = customer.getCustomerPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("classActiveShipping", true);

			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}

			model.addAttribute("emptyShippingList", false);

			return "checkout";
		}
	}

	@RequestMapping("/setPaymentMethod")
	public String setPaymentMethod(@RequestParam("userPaymentId") Long userPaymentId, Principal principal,
			Model model) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountRepository.findByUsername(username));
		customerPayment = customerService.findCustomerPaymentById(userPaymentId);

		if (customerShipping.getCustomer().getAccount().getId() != customer.getAccount().getId()) {
			return "badRequestPage";
		} else {

			List<CartProduct> cartProductList = oderService.findByShoppingCart(customer.getShopingCart());

			double total = 0;
			for (CartProduct cartProduct : cartProductList) {
				total += cartProduct.getQuanlity() * cartProduct.getProduct().getPrice();
			}
			model.addAttribute("total", total);
			model.addAttribute("shippingAddress", customerShipping);
			model.addAttribute("payment", customerPayment);

			model.addAttribute("cartItemList", cartProductList);
			model.addAttribute("shoppingCart", customer.getShopingCart());

			model.addAttribute("classActiveShipping", true);

			List<CustomerShipping> userShippingList = customer.getCustomerShippingList();
			List<CustomerPayment> userPaymentList = customer.getCustomerPaymentList();

			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);

			model.addAttribute("classActivePayment", true);

			if (userPaymentList.size() == 0) {
				model.addAttribute("emptyPaymentList", true);
			} else {
				model.addAttribute("emptyPaymentList", false);
			}

			model.addAttribute("emptyShippingList", false);

			return "checkout";
		}
	}
}
