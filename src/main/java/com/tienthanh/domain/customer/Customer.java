package com.tienthanh.domain.customer;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.tienthanh.domain.AbstractClass;
import com.tienthanh.domain.Account;
import com.tienthanh.domain.oder.ShoppingCart;

@Entity
public class Customer extends AbstractClass {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@OneToOne(cascade = CascadeType.ALL)
	private Account account;

	@OneToOne(cascade = CascadeType.ALL)
	private Address address;

	private boolean enabled = true;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<CustomerPayment> customerPaymentList;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<CustomerShipping> customerShippingList;

	@OneToOne(cascade = CascadeType.ALL)
	private ShoppingCart shopingCart;

	public Customer() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<CustomerPayment> getCustomerPaymentList() {
		return customerPaymentList;
	}

	public void setCustomerPaymentList(List<CustomerPayment> customerPaymentList) {
		this.customerPaymentList = customerPaymentList;
	}

	public List<CustomerShipping> getCustomerShippingList() {
		return customerShippingList;
	}

	public void setCustomerShippingList(List<CustomerShipping> customerShippingList) {
		this.customerShippingList = customerShippingList;
	}

	public ShoppingCart getShopingCart() {
		return shopingCart;
	}

	public void setShopingCart(ShoppingCart shopingCart) {
		this.shopingCart = shopingCart;
	}

}
