package com.example.demo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {

}
