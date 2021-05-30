package com.tienthanh.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.Book;
import com.tienthanh.domain.BookToCartItem;
import com.tienthanh.domain.CartItem;
import com.tienthanh.domain.ShoppingCart;
import com.tienthanh.domain.User;
import com.tienthanh.repository.BookToCartItemRepository;
import com.tienthanh.repository.CartItemRepository;
import com.tienthanh.service.CartItemService;

@Service
public class CartItemServiceImpl implements CartItemService{
	@Autowired
	CartItemRepository cartItemRepository;
	
	@Autowired
	BookToCartItemRepository bookToCartItemRepository;
	
	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}
	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		// TODO Auto-generated method stub
		BigDecimal bigDecimal= new BigDecimal(cartItem.getBook().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
		bigDecimal=bigDecimal.setScale(2);
		cartItem.setSubtotal(bigDecimal);
		cartItemRepository.save(cartItem);
		return cartItem;
	}
	@Override
	public CartItem addBookToCartItem(Book book, User user, int qty) {
		// TODO Auto-generated method stub
List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for (CartItem cartItem : cartItemList) {
			if(book.getId() == cartItem.getBook().getId()) {
				cartItem.setQty(cartItem.getQty()+qty);
				cartItem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);
		
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
		cartItem = cartItemRepository.save(cartItem);
		
		BookToCartItem bookToCartItem = new BookToCartItem();
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemRepository.save(bookToCartItem);
		
		return cartItem;
	}
	@Override
	public CartItem findById(Long id) {
		// TODO Auto-generated method stub
		return cartItemRepository.findById(id).get();
	}
	@Override
	public void removeCartItem(CartItem cartItem) {
		// TODO Auto-generated method stub
		bookToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
		return;
	}
	@Override
	public CartItem save(CartItem cartItem) {
		// TODO Auto-generated method stub
		return cartItemRepository.save(cartItem);
	}
	
}
