package com.robertocosta.dscatalog.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertocosta.dscatalog.dto.EmailDTO;
import com.robertocosta.dscatalog.dto.NewPasswordDTO;
import com.robertocosta.dscatalog.entities.PasswordRecover;
import com.robertocosta.dscatalog.entities.User;
import com.robertocosta.dscatalog.repositories.PasswordRecoverRepository;
import com.robertocosta.dscatalog.repositories.UserRepository;
import com.robertocosta.dscatalog.service.exceptions.ResourceNotFoundException;

@Service
public class AuthService {

	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private PasswordRecoverRepository passwordRecoverRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void createRecoverToken(EmailDTO dto) {

		User user = userRepository.findByEmail(dto.getEmail());
		if (user == null) {
			throw new ResourceNotFoundException("Email não encontrado");
		}

		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(dto.getEmail());
		entity.setToken(UUID.randomUUID().toString());
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
		entity = passwordRecoverRepository.save(entity);

		String body = "Acesse o link para definir uma nova senha\n\n"
				+ recoverUri + entity.getToken() +
				"\n\n Validade de "+ tokenMinutes + " minutos.";

		emailService.sendEmail(user.getEmail(), "Recuperação de senha", body);
	}
	
	@Transactional
	public void saveNewPassword(NewPasswordDTO dto) {
		List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(dto.getToken(), Instant.now());
		if(result.isEmpty()) {
			throw new ResourceNotFoundException("Token inválido");
		}
		
		User entity = userRepository.findByEmail(result.get(0).getEmail());
		entity.setPassword(passwordEncoder.encode(dto.getPassword()));
		entity = userRepository.save(entity);		
	}

}
