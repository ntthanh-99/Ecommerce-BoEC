package com.tienthanh.domain.oder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.tienthanh.domain.AbstractClass;
import com.tienthanh.domain.product.Product;

@Entity
public class SaleOffForProduct extends AbstractClass {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	private Product product;

	@OneToOne(cascade = CascadeType.ALL)
	private SaleOff saleOff;

	private double sale;

	public SaleOffForProduct() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public SaleOff getSaleOff() {
		return saleOff;
	}

	public void setSaleOff(SaleOff saleOff) {
		this.saleOff = saleOff;
	}

	public double getSale() {
		return sale;
	}

	public void setSale(double sale) {
		this.sale = sale;
	}

}
