package com.backend.domicare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.domicare.model.Token;

public interface TokensRepository extends JpaRepository<Token, Long> {
        public void deleteByRefreshToken(String refreshToken);
        public Token findByRefreshToken(String refreshToken);
}
