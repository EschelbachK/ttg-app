package com.traintogain.backend.passwordreset;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetRepository extends MongoRepository<com.traintogain.backend.auth.passwordreset.PasswordResetToken, String> {

    Optional<com.traintogain.backend.auth.passwordreset.PasswordResetToken> findByToken(String token);

    void deleteByUserId(String userId);
}