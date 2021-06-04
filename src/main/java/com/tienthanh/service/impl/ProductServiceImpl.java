package com.tienthanh.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.influx.InfluxDbOkHttpClientBuilderProvider;
import org.springframework.stereotype.Service;

import com.tienthanh.domain.product.Book;
import com.tienthanh.domain.product.Clothes;
import com.tienthanh.domain.product.Electronic;
import com.tienthanh.domain.product.Product;
import com.tienthanh.repository.BookRepository;
import com.tienthanh.repository.ClothesRepository;
import com.tienthanh.repository.ElectronicRepository;
import com.tienthanh.repository.ProductRepository;
import com.tienthanh.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ClothesRepository clothesRepository;

	@Autowired
	private ElectronicRepository electronicRepository;

	@Override
	public List<Product> findAllUnactiveProduct() {
		// TODO Auto-generated method stub
		List<Product> unactiveProducts = new ArrayList<>();
		List<Product> products = (List<Product>) productRepository.findAll();
		for (Product product : products) {
			if (product.isActive() == false) {
				unactiveProducts.add(product);
			}
		}
		return unactiveProducts;
	}

	@Override
	public Product saveProduct(Product product) {
		// TODO Auto-generated method stub
		return productRepository.save(product);
	}

	@Override
	public Product findById(Long id) {
		// TODO Auto-generated method stub
		return productRepository.findById(id).get();
	}

	@Override
	public Book findBookByProduct(Product product) {
		// TODO Auto-generated method stub
		return bookRepository.findByProduct(product);
	}

	@Override
	public Clothes findClothesByProduct(Product product) {
		// TODO Auto-generated method stub
		return clothesRepository.findByProduct(product);
	}

	@Override
	public Electronic findElectronicByProduct(Product product) {
		// TODO Auto-generated method stub
		return electronicRepository.findByProduct(product);
	}

	@Override
	public Book findBookById(Long id) {
		// TODO Auto-generated method stub
		return bookRepository.findById(id).get();
	}

	@Override
	public Clothes findClothesById(Long id) {
		// TODO Auto-generated method stub
		return clothesRepository.findById(id).get();
	}

	@Override
	public Electronic findElectronicById(Long id) {
		// TODO Auto-generated method stub
		return electronicRepository.findById(id).get();
	}

	@Override
	public Book saveBook(Book book) {
		// TODO Auto-generated method stub
		return bookRepository.save(book);
	}

	@Override
	public Clothes saveClothes(Clothes clothes) {
		// TODO Auto-generated method stub
		return clothesRepository.save(clothes);
	}

	@Override
	public Electronic saveElectronic(Electronic electronic) {
		// TODO Auto-generated method stub
		return electronicRepository.save(electronic);
	}

	@Override
	public List<Product> findAllActiveProduct() {
		// TODO Auto-generated method stub
		List<Product> activeProducts = new ArrayList<>();
		List<Product> products = (List<Product>) productRepository.findAll();
		for (Product product : products) {
			if (product.isActive() == true) {
				activeProducts.add(product);
			}
		}
		return activeProducts;
	}

	@Override
	public List<Book> findAllActiveBook() {
		// TODO Auto-generated method stub
		List<Book> activeBooks = new ArrayList<>();
		List<Book> books = (List<Book>) bookRepository.findAll();
		for (Book book : books) {
			if (book.getProduct().isActive() == true) {
				activeBooks.add(book);
			}
		}
		return activeBooks;
	}

	@Override
	public List<Electronic> findAllActiveElectronic() {
		// TODO Auto-generated method stub
		List<Electronic> activeElectronics = new ArrayList<>();
		List<Electronic> electronics = (List<Electronic>) electronicRepository.findAll();
		for (Electronic electronic : electronics) {
			if (electronic.getProduct().isActive() == true) {
				activeElectronics.add(electronic);
			}
		}
		return activeElectronics;
	}

	@Override
	public List<Clothes> findAllActiveClothes() {
		// TODO Auto-generated method stub
		List<Clothes> activeClothes = new ArrayList<>();
		List<Clothes> clothess = (List<Clothes>) clothesRepository.findAll();
		for (Clothes clothes : clothess) {
			if (clothes.getProduct().isActive() == true) {
				activeClothes.add(clothes);
			}
		}
		return activeClothes;
	}

}
