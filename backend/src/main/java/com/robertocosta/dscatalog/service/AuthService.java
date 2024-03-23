package com.robertocosta.dscatalog.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.robertocosta.dscatalog.dto.EmailDTO;
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
	private PasswordRecoverRepository passwordRecoverRepository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public void createRecoverToken(EmailDTO body) {

		User user = userRepository.findByEmail(body.getEmail());
		if (user == null) {
			throw new ResourceNotFoundException("Email não encontrado");
		}

		PasswordRecover entity = new PasswordRecover();
		entity.setEmail(body.getEmail());
		entity.setToken(UUID.randomUUID().toString());
		entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60));
		entity = passwordRecoverRepository.save(entity);

		String bodyEmail = "Acesse o link para definir uma nova senha\n\n"
				+ recoverUri + entity.getToken() +
				"\n\n Validade de "+ tokenMinutes + " minutos.";


		emailService.sendEmail(user.getEmail(), "Recuperação de senha", bodyEmail);

	}

}
