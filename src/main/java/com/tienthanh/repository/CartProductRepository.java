package com.tienthanh.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.oder.CartProduct;
import com.tienthanh.domain.oder.ShoppingCart;

public interface CartProductRepository extends CrudRepository<CartProduct, Long> {
	List<CartProduct> findByShoppingCart(ShoppingCart shoppingCart);

}
