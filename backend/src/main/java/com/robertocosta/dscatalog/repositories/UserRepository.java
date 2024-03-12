package com.robertocosta.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.robertocosta.dscatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT obj FROM User obj "
			+ "JOIN FETCH obj.roles ",
			countQuery = "SELECT COUNT(obj) FROM User obj JOIN obj.roles")
	Page<User> searchAll(Pageable pageable);
	
	User findByEmail(String email);
}
