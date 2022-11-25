package com.example.demo.runner;

import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.models.Role;
import com.example.demo.services.RoleService;

@Component
public class DatabaseInitializer implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger(DatabaseInitializer.class);

	private final RoleService roleService;

	public DatabaseInitializer(RoleService roleService) {
		super();
		this.roleService = roleService;
	}

	@Override
	public void run(String... args) throws Exception {
		if (!roleService.findAll().isEmpty())
			return;

		ROLE_LIST.forEach(roleService::create);

		logger.info("Database initialized");
	}

	private static final List<Role> ROLE_LIST = Arrays.asList(
			new Role("1", "ROLE_COMMUM"),
			new Role("2", "ROLE_ADMIN"));
}