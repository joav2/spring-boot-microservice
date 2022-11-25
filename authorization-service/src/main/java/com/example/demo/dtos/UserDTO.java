package com.example.demo.dtos;

import java.util.Date;
import java.util.List;

public record UserDTO(
		String id, 
		String fullname,
		String email,
		String username, 
		String password, 
		Date createdOn,
		List<String> roles) {
	
	public UserDTO() {
		this(null, null, null, null, null, null, null);
	}

	public UserDTO withId(String id) {
		return new UserDTO(id, fullname, email, username, password, createdOn, roles);
	}

	public UserDTO withFullname(String fullname) {
		return new UserDTO(id, fullname, email, username, password, createdOn, roles);
	}
	
	public UserDTO withEmail(String email) {
		return new UserDTO(id(), fullname(), email, username(), password(), createdOn(), roles());
	}

	public UserDTO withUsername(String username) {
		return new UserDTO(id(), fullname(), email(), username, password(), createdOn(), roles());
	}

	public UserDTO withPassword(String password) {
		return new UserDTO(id(), fullname(), email(), username(), password, createdOn(), roles());
	}

	public UserDTO withCreatedOn(Date createdOn) {
		return new UserDTO(id(), fullname(), email(), username(), password(), createdOn, roles());
	}
	
	public UserDTO withRoles(List<String> roles) {
		return new UserDTO(id, fullname, email, username, password, createdOn, roles);
	}
}