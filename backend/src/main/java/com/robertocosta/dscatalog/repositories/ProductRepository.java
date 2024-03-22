package com.robertocosta.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.robertocosta.dscatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(value = "SELECT DISTINCT obj FROM Product obj "
			+ "JOIN FETCH obj.categories ")
			//, countQuery = "SELECT COUNT(obj) FROM Product obj")
	Page<Product> searchAll(Pageable pageable);
	
	@Query(value = "SELECT DISTINCT obj FROM Product obj "
			+ "JOIN FETCH obj.categories cat "
			+ "WHERE cat.id IN(:categoryIds) "		
			+ "AND LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%'))")
	//, countQuery = "SELECT COUNT(obj) FROM Product obj")
	Page<Product> searchProducts(List<Long> categoryIds, String name, Pageable pageable);
}
