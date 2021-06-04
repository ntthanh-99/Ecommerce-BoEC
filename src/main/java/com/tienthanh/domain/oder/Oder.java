package com.tienthanh.domain.oder;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.tienthanh.domain.customer.Customer;

@Entity
public class Oder {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String oderStatus;
	
	private Double total;
	
	@OneToMany(mappedBy = "oder", cascade = CascadeType.ALL)
	private List<CartProduct> cartProductList;
	
	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToOne
	private Shipping shipping;

	public Oder() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOderStatus() {
		return oderStatus;
	}

	public void setOderStatus(String oderStatus) {
		this.oderStatus = oderStatus;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public List<CartProduct> getCartProductList() {
		return cartProductList;
	}

	public void setCartProductList(List<CartProduct> cartProductList) {
		this.cartProductList = cartProductList;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
}
