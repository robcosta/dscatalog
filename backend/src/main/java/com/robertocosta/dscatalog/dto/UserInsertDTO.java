package com.robertocosta.dscatalog.dto;

import com.robertocosta.dscatalog.service.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

	private String password;

	public UserInsertDTO() {
		super();
	}
	
	public String getPassword() {
		return password;
	}
	
	
}
