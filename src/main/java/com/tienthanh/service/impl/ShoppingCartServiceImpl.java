package com.tienthanh.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.CartItem;
import com.tienthanh.domain.ShoppingCart;
import com.tienthanh.repository.ShoppingCartRepository;
import com.tienthanh.service.CartItemService;
import com.tienthanh.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService{
	@Autowired
	private ShoppingCartRepository shoppingCartRepository;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Override
	public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		BigDecimal cartTotal= new BigDecimal(0);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		for (CartItem cartItem : cartItemList) {
			if(cartItem.getBook().getInStockNumber()>0) {
				cartItemService.updateCartItem(cartItem);
				cartTotal= cartTotal.add(cartItem.getSubtotal());
			}
		}
		shoppingCart.setGrandTotal(cartTotal);
		shoppingCartRepository.save(shoppingCart);
		return shoppingCart;
	}

	@Override
	public void clearShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for (CartItem cartItem : cartItemList) {
			cartItem.setShoppingCart(null);
			cartItemService.save(cartItem);
		}
		
		shoppingCart.setGrandTotal(new BigDecimal(0));
		
		shoppingCartRepository.save(shoppingCart);
	}
	
}
