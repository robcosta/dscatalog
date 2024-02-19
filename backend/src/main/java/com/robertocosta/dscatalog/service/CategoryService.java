package com.robertocosta.dscatalog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertocosta.dscatalog.dto.CategoryDTO;
import com.robertocosta.dscatalog.entities.Category;
import com.robertocosta.dscatalog.repositories.CategoryRepository;
import com.robertocosta.dscatalog.service.exceptions.ResourceNotFoundException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> result = repository.findAll();
		return result.stream().map(x -> new CategoryDTO(x)).toList();
	}
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Category result = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado"));
		return new CategoryDTO(result);
	}
}
