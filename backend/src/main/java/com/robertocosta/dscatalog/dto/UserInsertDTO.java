package com.robertocosta.dscatalog.dto;

import com.robertocosta.dscatalog.service.validation.UserInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

	@NotBlank(message = "Campo obrigatório")
	@Size(min = 8, message = "Deve ter no mínimo 8 caracteres")
	private String password;

	public UserInsertDTO() {
		super();
	}
	
	public String getPassword() {
		return password;
	}
	
	
}
