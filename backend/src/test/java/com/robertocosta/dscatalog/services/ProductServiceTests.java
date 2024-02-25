package com.robertocosta.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.robertocosta.dscatalog.repositories.ProductRepository;
import com.robertocosta.dscatalog.service.ProductService;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExisteID;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExisteID = 1000L;
		Mockito.doNothing().when(repository).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExixsts() {
		service.delete(existingId);
		
//		Assertions.assertDoesNotThrow(() -> {
//			service.delete(existingId);
//		});
//		
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
}
