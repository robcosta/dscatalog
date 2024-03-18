package com.robertocosta.dscatalog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.robertocosta.dscatalog.dto.CategoryDTO;
import com.robertocosta.dscatalog.entities.Category;
import com.robertocosta.dscatalog.repositories.CategoryRepository;
import com.robertocosta.dscatalog.service.exceptions.DatabaseException;
import com.robertocosta.dscatalog.service.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable){
		Page<Category> result = repository.findAll(pageable);
		return result.map(x -> new CategoryDTO(x));
	}
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Category result = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new CategoryDTO(result);
	}
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		if(repository.findByName(dto.getName()) != null) {
			throw new DatabaseException("Nome de categoria já existente");
		}
		Category entity = new Category();
		entity.setId(dto.getId());
		entity.setName(dto.getName());
		return new CategoryDTO(repository.save(entity));
	}
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getReferenceById(id);
			if(repository.findByName(dto.getName()) != null && entity.getId() != id) {
				throw new DatabaseException("Nome de categoria já existente");
			}
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {			
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if(!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");			
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}
	
}
