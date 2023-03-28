package com.gymbuddy.auth.persistence.repository.auth;

import com.gymbuddy.auth.persistence.domain.auth.AuthenticationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository
        extends JpaRepository<AuthenticationToken, Long> {
    @Query(value =
            " SELECT * FROM auth_token a" +
                    " WHERE a.token = :token" +
                    " AND a.token_type = :tokenType", nativeQuery = true)
    Optional<AuthenticationToken> findByToken(final String token, final String tokenType);

    @Query(value =
            " SELECT * FROM auth_token a" +
                    " WHERE a.user_id = :userId" +
                    " AND a.token_type = :tokenType" +
                    " LIMIT 1", nativeQuery = true)
    Optional<AuthenticationToken> findTokenByUserId(final Long userId, final String tokenType);
}
