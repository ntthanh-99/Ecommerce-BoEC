package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.product.Book;
import com.tienthanh.domain.product.Product;

public interface BookRepository extends CrudRepository<Book, Long> {
	Book findByProduct(Product product);

}
