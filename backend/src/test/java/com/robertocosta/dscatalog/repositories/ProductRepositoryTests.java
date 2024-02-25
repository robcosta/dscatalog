package com.robertocosta.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.robertocosta.dscatalog.entities.Product;
import com.robertocosta.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {

	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistId;
	private long countTotalProducts;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistId = 100L;
		countTotalProducts = 25;
		product = Factory.createProduct();
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenExistId() {
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertTrue(result.isPresent());
	}
	
	public void findByIdShouldReturnEmptyOptionalWhenDoesNotExistId() {
		Optional<Product> result = repository.findById(nonExistId);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void saveShoulPersistWithAutoincrementWhenIdIsNull() {
		product.setId(null);
		product = repository.save(product);
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);
		Optional<Product> result = repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());		
	}
	
	

}
