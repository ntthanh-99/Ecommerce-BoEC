package com.tienthanh.domain.oder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.tienthanh.domain.customer.CustomerShipping;
import com.tienthanh.domain.employee.Shipper;

@Entity
public class Shipping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	private Oder oder;

	@OneToOne
	private Shipper shipper;

	@OneToOne
	private CustomerShipping customerShipping;

	public Shipping() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Oder getOder() {
		return oder;
	}

	public void setOder(Oder oder) {
		this.oder = oder;
	}

	public Shipper getShipper() {
		return shipper;
	}

	public void setShipper(Shipper shipper) {
		this.shipper = shipper;
	}

	public CustomerShipping getCustomerShipping() {
		return customerShipping;
	}

	public void setCustomerShipping(CustomerShipping customerShipping) {
		this.customerShipping = customerShipping;
	}

}
