package com.robertocosta.dscatalog.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertocosta.dscatalog.dto.CategoryDTO;
import com.robertocosta.dscatalog.dto.ProductDTO;
import com.robertocosta.dscatalog.entities.Category;
import com.robertocosta.dscatalog.entities.Product;
import com.robertocosta.dscatalog.repositories.CategoryRepository;
import com.robertocosta.dscatalog.repositories.ProductRepository;
import com.robertocosta.dscatalog.service.exceptions.DatabaseException;
import com.robertocosta.dscatalog.service.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Transactional(readOnly = true)
	public Page<ProductDTO> searchAll(Pageable pageable) {
		Page<Product> result = repository.searchAll(pageable);
		return result.map(x -> new ProductDTO(x, x.getCategories()));
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Product result = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new ProductDTO(result, result.getCategories());
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		return new ProductDTO(repository.save(entity), entity.getCategories());
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity, entity.getCategories());
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(Instant.now());
		entity.getCategories().clear();
		for (CategoryDTO catDTO : dto.getCategories()) {
			Category cat = categoryRepository.getReferenceById(catDTO.getId());
			cat.setId(catDTO.getId());
			entity.getCategories().add(cat);
		}
		
//		dto.getCategories().forEach(catDTO -> entity.getCategories().add(new Category(catDTO.getId(), catDTO.getName())));
	}

}
