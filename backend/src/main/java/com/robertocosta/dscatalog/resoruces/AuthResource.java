package com.robertocosta.dscatalog.resoruces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.robertocosta.dscatalog.dto.EmailDTO;
import com.robertocosta.dscatalog.dto.NewPasswordDTO;
import com.robertocosta.dscatalog.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping(value = "/recover-token")
	public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO body) {
		authService.createRecoverToken(body);		
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping(value = "/new-password")
	public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO dto) {
		authService.saveNewPassword(dto);		
		return ResponseEntity.noContent().build();
	}

}
