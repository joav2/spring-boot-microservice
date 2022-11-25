package com.example.demo.services;

import com.example.demo.dtos.UserDTO;

public interface UserService {
	
	UserDTO create(final UserDTO userDTO);
	
	UserDTO findByUsername(String username);
	
	UserDTO findByEmail(String email);
	
	boolean hasUserWithUsername(String username);

    boolean hasUserWithEmail(String email);
}
