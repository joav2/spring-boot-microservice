package com.example.demo.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.RoleService;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;

	public RoleServiceImpl(RoleRepository roleRepository) {
		super();
		this.roleRepository = roleRepository;
	}

	@Override
	public void create(Role role) {
		roleRepository.save(role);
	}

	@Override
	public List<Role> findAll() {
		// TODO Auto-generated method stub
		return roleRepository.findAll();
	}
}
