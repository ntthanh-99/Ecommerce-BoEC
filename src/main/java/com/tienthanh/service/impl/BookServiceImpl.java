package com.tienthanh.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.Book;
import com.tienthanh.repository.BookRepository;
import com.tienthanh.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	@Autowired
	BookRepository bookRepository;

	@Override
	public List<Book> findAll() {
		// TODO Auto-generated method stub
		return (List<Book>) bookRepository.findAll();
	}

	@Override
	public Book findById(Long id) {
		// TODO Auto-generated method stub
		return bookRepository.findById(id).get();
	}
	
	
}
