package com.backend.domicare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.domicare.model.Token;

@Repository
public interface TokensRepository extends JpaRepository<Token, Long> {
        public void deleteByRefreshToken(String refreshToken);
        public Token findByRefreshToken(String refreshToken);
        public List<Token> findByUserId(Long userId);
        
        /**
         * Delete all tokens for a specific user
         * 
         * @param userId The user ID
         */
       
}
