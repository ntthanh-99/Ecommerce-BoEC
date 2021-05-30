package com.tienthanh.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.BookToCartItem;
import com.tienthanh.domain.CartItem;
@Transactional
public interface BookToCartItemRepository extends CrudRepository<BookToCartItem, Long>{
	void deleteByCartItem(CartItem cartItem);
}
