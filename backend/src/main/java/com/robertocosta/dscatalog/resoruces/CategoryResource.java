package com.robertocosta.dscatalog.resoruces;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robertocosta.dscatalog.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

	@GetMapping
	public ResponseEntity<List<Category>> findAll(){
		List<Category> result = new ArrayList<>();
		result.add(new Category(1L,"Books"));
		result.add(new Category(2L,"Computers"));
		result.add(new Category(2L,"Eletrics"));
		return ResponseEntity.ok().body(result);
	}
	
}
