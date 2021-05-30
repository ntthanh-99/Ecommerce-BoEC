package com.tienthanh.service;

import java.util.List;

import com.tienthanh.domain.Book;
import com.tienthanh.domain.CartItem;
import com.tienthanh.domain.ShoppingCart;
import com.tienthanh.domain.User;

public interface CartItemService {
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

	CartItem updateCartItem(CartItem cartItem);

	CartItem addBookToCartItem(Book book, User user, int qty);
	
	CartItem findById(Long id);
	
	void removeCartItem(CartItem cartItem);
	
	CartItem save(CartItem cartItem);
}
