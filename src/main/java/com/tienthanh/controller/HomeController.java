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

import com.tienthanh.domain.Book;
import com.tienthanh.domain.User;
import com.tienthanh.domain.UserBilling;
import com.tienthanh.domain.UserPayment;
import com.tienthanh.domain.UserShipping;
import com.tienthanh.domain.security.PasswordResetToken;
import com.tienthanh.domain.security.Role;
import com.tienthanh.domain.security.UserRole;
import com.tienthanh.service.BookService;
import com.tienthanh.service.UserPaymentService;
import com.tienthanh.service.UserService;
import com.tienthanh.service.UserShippingService;
import com.tienthanh.service.impl.UserSecurityService;
import com.tienthanh.utility.MailContructor;
import com.tienthanh.utility.SecurityUtility;
import com.tienthanh.utility.USConstants;

@Controller
public class HomeController {
	@Autowired
	UserService userService;

	@Autowired
	UserSecurityService userSecurityService;

	@Autowired
	BookService bookService;
	
	@Autowired
	UserPaymentService userPaymentService;
	
	@Autowired
	UserShippingService userShippingService;
	
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
		User user = userService.findByEmail(email);
		if (user == null) {
			model.addAttribute("emailNotExist", true);
			model.addAttribute("classActiveForgetPassword", true);
			return "myAccount";
		}
		String password = SecurityUtility.randomPassword();
		// encode password
		String encrytedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encrytedPassword);
		userService.save(user);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetToken(user, token);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage sentEmail = mailContructor.contructResetTokenEmail(appUrl, request.getLocale(), token, user,
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
		if (userService.findByUsername(username) != null) {
			model.addAttribute("usernameExists", true);
			return "myAccount";
		}
		if (userService.findByEmail(userEmail) != null) {
			model.addAttribute("emailExists", true);
			return "myAccount";
		}

		User user = new User();
		user.setEmail(userEmail);
		user.setUsername(username);
		// random password
		String password = SecurityUtility.randomPassword();
		// encode password
		String encrytedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encrytedPassword);
		//
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);

		String token = UUID.randomUUID().toString();
		userService.createPasswordResetToken(user, token);
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage email = mailContructor.contructResetTokenEmail(appUrl, request.getLocale(), token, user,
				password);
		mailSender.send(email);
		model.addAttribute("emailSent", true);
		return "myAccount";
	}

	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		// login with token
		PasswordResetToken passToken = userService.getPasswordResetToken(token);
		if (passToken == null) {
			String message = "Invalid Token";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
		}
		User user = passToken.getUser();
		String username = user.getUsername();

		UserDetails userDetails = userSecurityService.loadUserByUsername(username);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
				userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		model.addAttribute("user", user);
		model.addAttribute("classActiveEdit", true);

		return "myProfile";
	}
	
	@RequestMapping("myProfile")
	public String myProfile(Model model, Principal principal) {
		String username= principal.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAttribute("oderList", user.getOderList());
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping", userShipping);
		model.addAttribute("listOfCreditCard", true);
		model.addAttribute("listOfShippingAndresses", true);
		List<String> stateList =  USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveEdit", true);
		return "myProfile";
	}
	
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCard(Model model, Principal principal, HttpServletRequest request) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAttribute("oderList", user.getOderList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddress(Model model, Principal principal, HttpServletRequest request) {
		String username = principal.getName();
		User user = userService.findByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAtribute("oderList", user.getOderList());
		model.addAttribute("listOfShippingAddress", true);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAtribute("oderList", user.getOderList());
		model.addAttribute("addNewCreditCard", true);
		model.addAttribute("classActiveBilling", true);
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		
		List<String> stateList= USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		return "myProfile"; 
	}
	
	@PostMapping("/addNewCreditCard")
	public String addNewCreditCardPost(Model model, Principal principal, 
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling) {
		User user = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling,userPayment, user);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}
	
	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(Model model, Principal principal, @RequestParam("id") Long id) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment= userPaymentService.findById(id);
		
		if(user.getId()!= userPayment.getUser().getId())
			return "badRequestPage";
		else {
			model.addAttribute("user", user);
			UserBilling userBilling = userPayment.getUserBilling();
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("userBilling", userBilling);
			
			List<String> stateList= USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			model.addAttribute("addNewCreditCard", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			return "myProfile";
		}
		
	}
	
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(Model model, Principal principal, @RequestParam("id") Long id) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(id);
		if(user.getId() != userPayment.getUser().getId()) {
			return "badRequestPage";
		}
		else {
			model.addAttribute("user", user);
			userPaymentService.removeById(id);
			//user.getUserPaymentList().remove(userPayment);
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			model.addAttribute("listOfCreditCards", true);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("listOfShippingAddresses", true);
			return "myProfile";
		}
	}
	
	@RequestMapping(value= "/setDefaultPayment", method = RequestMethod.POST)
	public String setDefaultPayment(Model model, Principal principal, @ModelAttribute("defaultUserPaymentId") Long id) {
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(id);
		userService.setUserDefaultPayment(id, user);
		
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}
	
	@PostMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal, 
			@ModelAttribute("userShipping") UserShipping userShipping) {
		User user = userService.findByUsername(principal.getName());
		userService.updateUserShipping (userShipping, user);
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		return "myProfile";
	}
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user", user);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		//model.addAtribute("oderList", user.getOderList());
		model.addAttribute("addNewShippingAddress", true);
		model.addAttribute("classActiveShipping", true);
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping", userShipping);
		
		List<String> stateList= USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList", stateList);
		return "myProfile"; 
	}
	
	@RequestMapping("/updateUserShipping")
	public String updateUserShipping(
			@ModelAttribute("id") Long shippingAddressId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(shippingAddressId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			model.addAttribute("userShipping", userShipping);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("addNewShippingAddress", true);
			model.addAttribute("classActiveShipping", true);
			model.addAttribute("listOfCreditCards", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			return "myProfile";
		}
	}
	
	@RequestMapping(value="/setDefaultShippingAddress", method=RequestMethod.POST)
	public String setDefaultShippingAddress(
			@ModelAttribute("defaultShippingAddressId") Long defaultShippingId, Principal principal, Model model
			) {
		User user = userService.findByUsername(principal.getName());
		userService.setUserDefaultShipping(defaultShippingId, user);
		
		model.addAttribute("user", user);
		model.addAttribute("listOfCreditCards", true);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("listOfShippingAddresses", true);
		
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("userShippingList", user.getUserShippingList());
		
		return "myProfile";
	}
	
	@RequestMapping("/removeUserShipping")
	public String removeUserShipping(
			@ModelAttribute("id") Long userShippingId, Principal principal, Model model
			){
		User user = userService.findByUsername(principal.getName());
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if(user.getId() != userShipping.getUser().getId()) {
			return "badRequestPage";
		} else {
			model.addAttribute("user", user);
			
			userShippingService.removeById(userShippingId);
			
			model.addAttribute("listOfShippingAddresses", true);
			model.addAttribute("classActiveShipping", true);
			
			model.addAttribute("userPaymentList", user.getUserPaymentList());
			model.addAttribute("userShippingList", user.getUserShippingList());
			
			return "myProfile";
		}
	}
	
	@RequestMapping("/bookshelf")
	public String bookShelf(Model model) {
		List<Book> bookList= bookService.findAll();
		model.addAttribute("bookList", bookList);
		return "bookshelf";
	}
	@RequestMapping("/bookDetail")
	public String bookDetail(@RequestParam("id") Long id, Model model, Principal pincipal) {
		if(pincipal != null) {
			String username= pincipal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user",user);
		}
		Book book = bookService.findById(id);
		model.addAttribute("book", book);
		List<Integer> qtyList= Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		model.addAttribute("qtyList", qtyList);
		model.addAttribute("qty", 1);
		return "bookDetail";
	}
}
