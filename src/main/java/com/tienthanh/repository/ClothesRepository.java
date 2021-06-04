package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.product.Clothes;
import com.tienthanh.domain.product.Product;

public interface ClothesRepository extends CrudRepository<Clothes, Long> {
	Clothes findByProduct(Product product);

}
