package com.robertocosta.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.robertocosta.dscatalog.entities.Category;


public interface CategoryRepository extends JpaRepository<Category, Long> {

}
