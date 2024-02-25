package com.robertocosta.dscatalog.tests;

import java.time.Instant;

import com.robertocosta.dscatalog.dto.ProductDTO;
import com.robertocosta.dscatalog.entities.Category;
import com.robertocosta.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product =  new Product(1l,"Phone", "Good phone", 800.0, "https://img.com/big.jpg", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Electronics"));
		return product;
	}
	
	public static ProductDTO createProducDTO() {
		Product product = createProduct();
		return new ProductDTO(product, product.getCategories());
	}
}
