package com.example.demo.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.models.redis.TokenEntity;

public interface TokenEntityRepository extends CrudRepository<TokenEntity, String> {

}
