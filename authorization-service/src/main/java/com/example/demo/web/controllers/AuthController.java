package com.example.demo.web.controllers;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.AuthResponse;
import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.SingUpRequest;
import com.example.demo.dtos.UserDTO;
import com.example.demo.exception.DuplicatedUserInfoException;
import com.example.demo.models.redis.TokenEntity;
import com.example.demo.security.TokenProvider;
import com.example.demo.services.TokenEntityService;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final UserService userService;
	private final TokenProvider tokenProvider;
	private final AuthenticationManager authenticationManager;
	private final TokenEntityService tokenEntityService;
	
	public AuthController(UserService userService, TokenProvider tokenProvider,
			AuthenticationManager authenticationManager, TokenEntityService tokenEntityService) {
		super();
		this.userService = userService;
		this.tokenProvider = tokenProvider;
		this.authenticationManager = authenticationManager;
		this.tokenEntityService = tokenEntityService;
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
		String token = authenticateAndGetToken(loginRequest.username(), loginRequest.password());
		
		return ResponseEntity.ok(new AuthResponse(token));
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SingUpRequest signUpRequest) {
		if (userService.hasUserWithUsername(signUpRequest.username())) throw new DuplicatedUserInfoException(String.format("Username %s already been used", signUpRequest.username()));
		if (userService.hasUserWithEmail(signUpRequest.email())) throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.email()));
		
		userService.create(mapSignUpRequestToUserDTO(signUpRequest));
		
		String token = authenticateAndGetToken(signUpRequest.username(), signUpRequest.password());
		return ResponseEntity.ok(new AuthResponse(token));
	}
	
	private String authenticateAndGetToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        String token1 = tokenProvider.generateToken(authentication);
        String token2 = tokenEntity(authentication, token1);
        return token2;
    }
	
	private String tokenEntity(Authentication authentication, String token) {
		TokenEntity tokenEntity = new TokenEntity();
		tokenEntity.setId(UUID.randomUUID().toString());
		tokenEntity.setAuthenticationToken(token);
		tokenEntity.setUsername(authentication.getName());
		tokenEntity.setCreatedBy("SYSTEM");
		tokenEntity.setCreatedOn(LocalDateTime.now());
		tokenEntity.setModifiedBy("SYSTEM");
		tokenEntity.setModifiedOn(LocalDateTime.now());
		
		return tokenEntityService.create(tokenEntity).getId();
	}
	
	private UserDTO mapSignUpRequestToUserDTO(SingUpRequest signUpRequest) {
        return new UserDTO(null,
                signUpRequest.fullname(),
                signUpRequest.email(),
                signUpRequest.username(),
                signUpRequest.password(),
                new Date(),
                null);
    }
	
}
