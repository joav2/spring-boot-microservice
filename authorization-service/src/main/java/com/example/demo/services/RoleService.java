package com.example.demo.services;

import java.util.List;

import com.example.demo.models.Role;

public interface RoleService {

	void create(Role role);
	
	List<Role> findAll();
}
