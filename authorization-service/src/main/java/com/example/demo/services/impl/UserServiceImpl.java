package com.example.demo.services.impl;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.dtos.UserDTO;
import com.example.demo.models.Role;
import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;

	public UserServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	@Transactional(readOnly = false)
	public UserDTO create(final UserDTO userDTO) {
		final User user = new User();
		mapToEntity(userDTO, user);
		user.setRoles(ROLES_COMUM);
		String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword("{bcrypt}"+encodedPassword);
		return mapToDTO(userRepository.save(user), new UserDTO());
	}
	
	@Override
	public UserDTO findByUsername(String username) {
		return userRepository.findByUsername(username)
				.map(user -> mapToDTO(user, new UserDTO()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@Override
	public UserDTO findByEmail(String email) {
		return userRepository.findByEmail(email)
				.map(user -> mapToDTO(user, new UserDTO()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	@Override
	public boolean hasUserWithUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public boolean hasUserWithEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
		List<String> roleNames = user.getRoles()
                .stream()
                .map(Role::getRole)
                .toList();

		return new UserDTO(
				user.getId(),
				user.getFullname(),
				user.getEmail(),
				user.getUsername(),
				user.getPassword(),
				user.getCreatedOn(),
				roleNames
				);
	}
	
	private User mapToEntity(final UserDTO userDTO, final User user) {
		user.setFullname(userDTO.fullname());
		user.setUsername(userDTO.username());
		user.setPassword(userDTO.password());
		user.setCreatedOn(userDTO.createdOn());
		return user;
	}
	
	private final static List<Role> ROLES_COMUM = Arrays.asList(
			new Role("1", "ROLE_COMUM")
	);
}
