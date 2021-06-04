package com.tienthanh.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tienthanh.domain.Account;
import com.tienthanh.domain.customer.Address;
import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.customer.CustomerPayment;
import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.customer.Payment;
import com.tienthanh.domain.product.Book;
import com.tienthanh.domain.product.Clothes;
import com.tienthanh.domain.product.Electronic;
import com.tienthanh.domain.security.AccountRole;
import com.tienthanh.domain.security.PasswordResetToken;
import com.tienthanh.domain.security.Role;
import com.tienthanh.service.AccountService;
import com.tienthanh.service.CustomerService;
import com.tienthanh.service.ProductService;
import com.tienthanh.service.UserSecurityService;
import com.tienthanh.utility.MailContructor;
import com.tienthanh.utility.SecurityUtility;
import com.tienthanh.utility.USConstants;

@Controller
public class HomeController {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private UserSecurityService userSecurityService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private ProductService productService;
	
	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailContructor mailContructor;

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}

	@RequestMapping("/forgetPassword")
	public String forgetPassword(HttpServletRequest request, @ModelAttribute("email") String email, Model model) {
		Account account = accountService.findByEmail(email);
		if (account == null) {
			model.addAttribute("emailNotExist", true);
			model.addAttribute("classActiveForgetPassword", true);
			return "myAccount";
		}
		String password = SecurityUtility.randomPassword();
		// encode password
		String encrytedPassword = SecurityUtility.passwordEncoder().encode(password);
		account.setPassword(encrytedPassword);
		accountService.save(account);

		String token = UUID.randomUUID().toString();
		accountService.createPasswordResetToken(account, token);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage sentEmail = mailContructor.contructResetTokenEmail(appUrl, request.getLocale(), token,
				account,
				password);
		mailSender.send(sentEmail);
		model.addAttribute("forgetPasswordEmailSent", true);
		model.addAttribute("classActiveForgetPassword", true);
		return "myAccount";
	}

	@PostMapping("/newUser")
	public String newUserPost(HttpServletRequest request, @ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", userEmail);
		model.addAttribute("username", username);
		if (accountService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			return "myAccount";
		}
		if (accountService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			return "myAccount";
		}

		Account account = new Account();
		account.setEmail(userEmail);
		account.setUsername(username);
		// random password
		String password = SecurityUtility.randomPassword();
		// encode password
		String encrytedPassword = SecurityUtility.passwordEncoder().encode(password);
		account.setPassword(encrytedPassword);
		//
		Customer customer = new Customer();
		customer.setAccount(account);
		Role role = new Role();
		role.setId(1);
		role.setName("ROLE_USER");
		Set<AccountRole> accountRoles = new HashSet();
		accountRoles.add(new AccountRole(role, account));
		customerService.createCustomer(customer, accountRoles);

		String token = UUID.randomUUID().toString();
		accountService.createPasswordResetToken(account, token);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage email = mailContructor.contructResetTokenEmail(appUrl, request.getLocale(), token, account,
				password);
		mailSender.send(email);
		model.addAttribute("emailSent", true);
		return "myAccount";
	}

	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		// login with token
		PasswordResetToken passToken = accountService.getPasswordResetToken(token);
		if (passToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}
		Account account = passToken.getAccount();
		String username = account.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", account);
		model.addAttribute("classActiveEdit", true);

		return "myProfile";
	}

	@RequestMapping("myProfile")
	public String myProfile(Model model, Principal principal) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		model.addAttribute("user", customer);
		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());
		// model.addAttribute("oderList", user.getOderList());
		CustomerShipping userShipping = new CustomerShipping();
		model.addAttribute("userShipping", userShipping);
		model.addAttribute("listOfCreditCard", true);
		model.addAttribute("listOfShippingAndresses", true);
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);
		return "myProfile";
	}

	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCard(Model model, Principal principal, HttpServletRequest request) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		model.addAttribute("user", customer);
		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());
		// model.addAttribute("oderList", user.getOderList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}

	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddress(Model model, Principal principal, HttpServletRequest request) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		model.addAttribute("user", customer);
		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());
		// model.addAtribute("oderList", user.getOderList());
		model.addAttribute("listOfShippingAddress", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}

	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		model.addAttribute("user", customer);
		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());
		// model.addAtribute("oderList", user.getOderList());
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);

		Payment payment = new Payment();
		CustomerPayment customerPayment = new CustomerPayment();
		customerPayment.setPayment(payment);

		model.addAttribute("userPayment", customerPayment);
		return "myProfile";
	}

	@PostMapping("/addNewCreditCard")
	public String addNewCreditCardPost(Model model, Principal principal,
			@ModelAttribute("userPayment") CustomerPayment customerPayment) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		customerService.updateCustomerPayment(customerPayment, customer);
		model.addAttribute("user", customer);
		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}

	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(Model model, Principal principal, @RequestParam("id") Long id) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		CustomerPayment customerPayment = customerService.findCustomerPaymentById(id);

		if (customer.getAccount().getId() != customerPayment.getCustomer().getAccount().getId())
			return "badRequestPage";
		else {
			model.addAttribute("user", customer);
			model.addAttribute("userPayment", customerPayment);

			model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
			model.addAttribute("userShippingList", customer.getCustomerShippingList());

			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			return "myProfile";
		}

	}

	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(Model model, Principal principal, @RequestParam("id") Long id) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		CustomerPayment customerPayment = customerService.findCustomerPaymentById(id);
		if (customer.getAccount().getId() != customerPayment.getCustomer().getAccount().getId()) {
			return "badRequestPage";
		}
		else {
			model.addAttribute("user", customer);
			customerService.removeCustomerPaymentById(id);
			
			model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
			model.addAttribute("userShippingList", customer.getCustomerShippingList());

			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			return "myProfile";
		}
	}

	@RequestMapping(value = "/setDefaultPayment", method = RequestMethod.POST)
	public String setDefaultPayment(Model model, Principal principal, @ModelAttribute("defaultUserPaymentId") Long id) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		CustomerPayment customerPayment = customerService.findCustomerPaymentById(id);
		customerService.setCustomerDefaultPayment(id, customer);

		model.addAttribute("user", customer);

		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());

		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}

	@PostMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal,
			@ModelAttribute("userShipping") CustomerShipping customerShipping) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		customerService.updateCustomerShipping(customerShipping, customer);
		model.addAttribute("user", customer);
		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}

	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		model.addAttribute("user", customer);
		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());
		// model.addAtribute("oderList", user.getOderList());
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		Address address = new Address();
		CustomerShipping customerShipping = new CustomerShipping();
		customerShipping.setAddress(address);

		model.addAttribute("userShipping", customerShipping);
		return "myProfile";
	}

	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(@ModelAttribute("id") Long customerShippingId, Principal principal, Model model) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		CustomerShipping customerShipping = customerService.findCustomerShippingById(customerShippingId);

		if (customer.getAccount().getId() != customerShipping.getCustomer().getAccount().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", customer);

			model.addAttribute("userShipping", customerShipping);

			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);

			model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
			model.addAttribute("userShippingList", customer.getCustomerShippingList());

			return "myProfile";
		}
	}

	@RequestMapping(value = "/setDefaultShippingAddress", method = RequestMethod.POST)
	public String setDefaultShippingAddress(@ModelAttribute("defaultShippingAddressId") Long defaultShippingId,
			Principal principal, Model model) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		customerService.setCustomerDefaultShipping(defaultShippingId, customer);

		model.addAttribute("user", customer);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);

		model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
		model.addAttribute("userShippingList", customer.getCustomerShippingList());

		return "myProfile";
	}

	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(@ModelAttribute("id") Long customerShippingId, Principal principal, Model model) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountService.findByUsername(username));
		CustomerShipping customerShipping = customerService.findCustomerShippingById(customerShippingId);

		if (customer.getAccount().getId() != customerShipping.getCustomer().getAccount().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", customer);

			customerService.removeCustomerShippingById(customerShippingId);

			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);

			model.addAttribute("userPaymentList", customer.getCustomerPaymentList());
			model.addAttribute("userShippingList", customer.getCustomerShippingList());

			return "myProfile";
		}
	}

	@RequestMapping("/book")
	public String bookShelf(Model model) {
		List<Book> bookList = productService.findAllActiveBook();
		model.addAttribute("bookList", bookList);
		return "bookshelf";
	}

	@RequestMapping("/electronic")
	public String electronic(Model model) {
		List<Electronic> electronicList = productService.findAllActiveElectronic();
		model.addAttribute("electronicList", electronicList);
		return "electronicshelf";
	}

	@RequestMapping("/clothes")
	public String clothes(Model model) {
		List<Clothes> clothesList = productService.findAllActiveClothes();
		model.addAttribute("clothesList", clothesList);
		return "clothesshelf";
	}

	@RequestMapping("/bookDetail")
	public String bookDetail(@RequestParam("id") Long id, Model model, Principal principal) {
		if (principal != null) {
			String username = principal.getName();
			Customer customer = customerService.findByAccount(accountService.findByUsername(username));
			model.addAttribute("user", customer);
		}
		Book book = productService.findBookById(id);
		model.addAttribute("book", book);
		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		return "bookDetail";
	}
}
