package com.tienthanh.repository;

import org.springframework.data.repository.CrudRepository;

import com.tienthanh.domain.Book;

public interface BookRepository extends CrudRepository<Book, Long>{

}
