package com.robertocosta.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.robertocosta.dscatalog.dto.ProductDTO;
import com.robertocosta.dscatalog.entities.Category;
import com.robertocosta.dscatalog.entities.Product;
import com.robertocosta.dscatalog.repositories.CategoryRepository;
import com.robertocosta.dscatalog.repositories.ProductRepository;
import com.robertocosta.dscatalog.service.ProductService;
import com.robertocosta.dscatalog.service.exceptions.DatabaseException;
import com.robertocosta.dscatalog.service.exceptions.ResourceNotFoundException;
import com.robertocosta.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	@Mock
	private CategoryRepository categoryRepository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private ProductDTO productDTO;
	private Category category;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 2L;
		product = Factory.createProduct();
		productDTO = Factory.createProducDTO();
		category = Factory.createCategory();
		
		page = new PageImpl<>(List.of(product));
		
		//Mochito => Simula o comportamento do "repository"
				
		//Repository.searchAll (retorna algo (página de produtos)) - Primeiro o 'when' e depois a ação)
		Mockito.when(repository.searchAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		//Repository.save  (Retorna um produto)
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		//Repository.getRefenceById (Retorna um produto)
		Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
		Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
//		Mockito.doThrow(ResourceNotFoundException.class).when(repository).getReferenceById(nonExistingId);
		
		Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
//		Mockito.when(CategoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		
		
		
		//Repsoitory.findById (Retorna um Optional de Product)
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		//Mockito.doThrow(ResourceNotFoundException.class).when(repository.findById(nonExistingId));
		
		//Repository.deteById (retorna void - Primeiro a ação para depois o 'wneh')
		//Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
		Mockito.when(repository.existsById(dependentId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void findByIdShouldProducDTOtWhenIdExisits() {
		productDTO = service.findById(existingId);
		Assertions.assertNotNull(productDTO);
		Mockito.verify(repository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldTrowsResourceNotFoundExceptiontWhenIdDoesNotExisting() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void searchAllShouldReturnPage() {
		Pageable pageable =  PageRequest.of(0, 10);
		Page<ProductDTO> result = service.searchAll(pageable);
		Assertions.assertNotNull(result);
		Mockito.verify(repository).searchAll(pageable);
	}
	
	@Test
	public void updateShouldProducDTOWhenIdExisits() {
		productDTO = service.update(existingId, productDTO);
		Assertions.assertNotNull(productDTO);
		Mockito.verify(repository).save(product);
	}
	
	@Test
	public void updateShouldTrhowsResourceNotFoundExceptionWhenIdDoesNotExisits() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.update(nonExistingId, productDTO);
		});
	}
	
	@Test
	public void deleteShouldTrowsResouceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldTrowsDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}
	
}
