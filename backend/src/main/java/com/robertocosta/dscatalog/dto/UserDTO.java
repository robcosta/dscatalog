package com.robertocosta.dscatalog.dto;

import java.util.HashSet;
import java.util.Set;

import com.robertocosta.dscatalog.entities.Role;
import com.robertocosta.dscatalog.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {

	private Long id;
	
	@NotBlank(message = "Campo obrigatório")
	private String firstName;
	
	private String lastName;
	
	@Email(message = "Ensira um email válido")
	private String email;

	Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO() {
		
	}

	public UserDTO(Long id, @NotBlank(message = "Campo obrigatório") String firstName, String lastName,
			@Email(message = "Ensira um email válido") String email) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public UserDTO(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}
	
	public UserDTO(User entity, Set<Role> roles) {
		this(entity);
		roles.forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}
	
	public String getEmail() {
		return email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
			
}
