package com.tienthanh.service;

import java.util.List;

import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.customer.CustomerPayment;
import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.oder.CartProduct;
import com.tienthanh.domain.oder.Oder;
import com.tienthanh.domain.oder.ShoppingCart;
import com.tienthanh.domain.product.Product;

public interface OderService {
	List<CartProduct> findByShoppingCart(ShoppingCart shoppingCart);

	CartProduct findCartProductById(Long id);

	CartProduct addProductToCartItem(Product product, Customer customer, int qty);

	void removeCartProduct(CartProduct cartProduct);

	CartProduct updateCartProduct(CartProduct cartProduct);

	Oder createOrder(ShoppingCart shoppingCart, CustomerShipping customerShipping, CustomerPayment customerPayment,
			Customer customer);

	void clearShoppingCart(ShoppingCart shoppingCart);

	CartProduct saveCartProduct(CartProduct cartProduct);

	ShoppingCart saveShoppingCart(ShoppingCart shoppingCart);

}
