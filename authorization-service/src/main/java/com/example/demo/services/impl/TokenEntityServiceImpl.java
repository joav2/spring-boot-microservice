package com.example.demo.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.models.redis.TokenEntity;
import com.example.demo.repositories.TokenEntityRepository;
import com.example.demo.services.TokenEntityService;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class TokenEntityServiceImpl implements TokenEntityService {
	
	private final TokenEntityRepository tokenEntityRepository;

	public TokenEntityServiceImpl(TokenEntityRepository tokenEntityRepository) {
		super();
		this.tokenEntityRepository = tokenEntityRepository;
	}

	@Override
	@Transactional(readOnly = false)
	public TokenEntity create(TokenEntity tokenEntity) {
		// TODO Auto-generated method stub
		return tokenEntityRepository.save(tokenEntity);
	}

	@Override
	public Optional<TokenEntity> findById(String id) {
		// TODO Auto-generated method stub
		return tokenEntityRepository.findById(id);
	}

	@Override
	public Iterable<TokenEntity> findAll() {
		// TODO Auto-generated method stub
		return tokenEntityRepository.findAll();
	}

}
