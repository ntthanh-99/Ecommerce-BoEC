package com.tienthanh.service;

import java.util.List;

import com.tienthanh.domain.Book;

public interface BookService {
	List<Book> findAll();
	
	Book findById(Long id);
}
