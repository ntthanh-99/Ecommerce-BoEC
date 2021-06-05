package com.tienthanh.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.oder.CartProduct;
import com.tienthanh.domain.oder.ShoppingCart;
import com.tienthanh.domain.product.Book;
import com.tienthanh.domain.product.Product;
import com.tienthanh.repository.AccountRepository;
import com.tienthanh.service.CustomerService;
import com.tienthanh.service.OderService;
import com.tienthanh.service.ProductService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private OderService oderService;

	@Autowired
	private ProductService productService;

	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountRepository.findByUsername(username));


		ShoppingCart shoppingCart = customer.getShopingCart();

		List<CartProduct> cartProductList = oderService.findByShoppingCart(shoppingCart);

		model.addAttribute("cartProductList", cartProductList);
		model.addAttribute("shoppingCart", shoppingCart);

		return "shoppingCart";
	}

	@RequestMapping("/addItem")
	public String addItem(@ModelAttribute("book") Book book, @ModelAttribute("qty") String qty, Model model,
			Principal principal) {
		String username = principal.getName();
		Customer customer = customerService.findByAccount(accountRepository.findByUsername(username));
		book = productService.findBookById(book.getId());
		Product product = book.getProduct();

		if (Integer.parseInt(qty) > product.getQuanlity()) {
			model.addAttribute("notEnoughStock", true);
			return "forward:/bookDetail?id=" + product.getId();
		}

		CartProduct cartProduct = oderService.addProductToCartItem(product, customer, Integer.parseInt(qty));
		model.addAttribute("addBookSuccess", true);

		return "forward:/bookDetail?id=" + book.getId();
	}

	@RequestMapping("/updateCartItem")
	public String updateShoppingCart(@ModelAttribute("id") Long cartItemId, @ModelAttribute("qty") int qty) {
		CartProduct cartItem = oderService.findCartProductById(cartItemId);
		cartItem.setQuanlity(qty);
		oderService.updateCartProduct(cartItem);

		return "forward:/shoppingCart/cart";
	}

	@RequestMapping("/removeItem")
	public String removeItem(@RequestParam("id") Long id) {
		oderService.removeCartProduct(oderService.findCartProductById(id));
		return "forward:/shoppingCart/cart";
	}
}
