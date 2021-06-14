package com.tienthanh.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.jaxws.SimpleHttpServerJaxWsServiceExporter;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.customer.Customer;
import com.tienthanh.domain.customer.CustomerPayment;
import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.oder.CartProduct;
import com.tienthanh.domain.oder.Oder;
import com.tienthanh.domain.oder.Shipping;
import com.tienthanh.domain.oder.ShoppingCart;
import com.tienthanh.domain.product.Product;
import com.tienthanh.repository.CartProductRepository;
import com.tienthanh.repository.OderRepository;
import com.tienthanh.repository.ShoppingCartRepository;
import com.tienthanh.service.CustomerService;
import com.tienthanh.service.OderService;

@Service
public class OderServiceImpl implements OderService {
	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartProductRepository cartProductRepository;

	@Autowired
	private FormatDateImpl formatDate;

	@Autowired
	private OderRepository oderRepository;

	@Autowired
	private ShoppingCartRepository shoppingCartRepository;

	@Override
	public List<CartProduct> findByShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		return cartProductRepository.findByShoppingCart(shoppingCart);
	}

	@Override
	public CartProduct addProductToCartItem(Product product, Customer customer, int qty) {
		// TODO Auto-generated method stub
		if (customer.getShopingCart() == null) {
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setCustomer(customer);
			customer.setShopingCart(shoppingCart);
			saveShoppingCart(shoppingCart);
			customerService.saveCustomer(customer);
		}
		List<CartProduct> cartProducts = findByShoppingCart(customer.getShopingCart());
		// san pham da them roi
		for (CartProduct cartProduct : cartProducts) {
			if (product.getId() == cartProduct.getProduct().getId()) {
				cartProduct.setQuanlity(cartProduct.getQuanlity() + qty);
				cartProductRepository.save(cartProduct);
				return cartProduct;
			}
		}
		// san pham moi
		ShoppingCart shoppingCart = findShoppingCartByCustomer(customer);
		CartProduct cartItem = new CartProduct();
		cartItem.setShoppingCart(customer.getShopingCart());
		cartItem.setProduct(product);
		cartItem.setShoppingCart(shoppingCart);
		cartItem.setQuanlity(qty);
		cartItem = cartProductRepository.save(cartItem);
		return cartItem;

	}

	@Override
	public void removeCartProduct(CartProduct cartProduct) {
		// TODO Auto-generated method stub
		cartProductRepository.delete(cartProduct);
	}

	@Override
	public CartProduct findCartProductById(Long id) {
		// TODO Auto-generated method stub
		return cartProductRepository.findById(id).get();
	}

	@Override
	public CartProduct updateCartProduct(CartProduct cartProduct) {
		// TODO Auto-generated method stub
		return cartProductRepository.save(cartProduct);
	}

	@Override
	public Oder createOrder(ShoppingCart shoppingCart, CustomerShipping customerShipping,
			CustomerPayment customerPayment, Customer customer) {
		// TODO Auto-generated method stub
		Oder oder = new Oder();
		oder.setCustomer(customer);
		oder.setOderStatus("da thanh toan");
		Shipping shipping = new Shipping();
		shipping.setCustomerShipping(customerShipping);
		shipping.setOder(oder);
		customerShipping.setCustomer(customer);
		oder.setShipping(shipping);
		double total = 0;

		List<CartProduct> cartProducts = findByShoppingCart(shoppingCart);

		for (CartProduct cartProduct : cartProducts) {
			cartProduct.setShoppingCart(shoppingCart);
			cartProduct.getProduct().setQuanlity(cartProduct.getProduct().getQuanlity() - cartProduct.getQuanlity());
			total += cartProduct.getQuanlity() * cartProduct.getProduct().getPrice();

		}
		oder.setTotal(total);
		oder.setCreateDate(formatDate.convertLocalDateTimeToDate(LocalDateTime.now()));
		oder = oderRepository.save(oder);

		return oder;
	}

	@Override
	public void clearShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		List<CartProduct> cartItemList = findByShoppingCart(shoppingCart);

		for (CartProduct cartItem : cartItemList) {
			cartItem.setShoppingCart(null);
			saveCartProduct(cartItem);
		}

		saveShoppingCart(shoppingCart);

	}

	@Override
	public CartProduct saveCartProduct(CartProduct cartProduct) {
		// TODO Auto-generated method stub
		return cartProductRepository.save(cartProduct);
	}

	@Override
	public ShoppingCart saveShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		return shoppingCartRepository.save(shoppingCart);
	}

	@Override
	public ShoppingCart findShoppingCartByCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return shoppingCartRepository.findByCustomer(customer);
	}

	@Override
	public List<Oder> getOderforCustomer(Customer customer) {
		// TODO Auto-generated method stub
		return oderRepository.findByCustomer(customer);
	}

}
