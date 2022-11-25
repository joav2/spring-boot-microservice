package com.example.demo.services;

import java.util.Optional;

import com.example.demo.models.redis.TokenEntity;

public interface TokenEntityService {

	TokenEntity create(TokenEntity tokenEntity);
	Optional<TokenEntity> findById(String id);
	Iterable<TokenEntity> findAll();
}
