package com.gymbuddy.auth.persistence.query;

import com.gymbuddy.auth.dto.TokenType;
import com.gymbuddy.auth.persistence.domain.QUser;
import com.gymbuddy.auth.persistence.domain.auth.AuthenticationToken;
import com.gymbuddy.auth.persistence.domain.auth.QAuthenticationToken;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Query class for token entity.
 */
@Component
@AllArgsConstructor
public class TokenEntityMapper {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QAuthenticationToken AUTHENTICATION_TOKEN = QAuthenticationToken.authenticationToken;
    private static final QUser USER = QUser.user;

    public Optional<AuthenticationToken> getToken(final String token, final String tokenType) {
        final JPAQuery<AuthenticationToken> query = new JPAQuery<>(entityManager);
        return Optional.ofNullable(
                query.from(AUTHENTICATION_TOKEN)
                        .where(
                                AUTHENTICATION_TOKEN.token.eq(token),
                                AUTHENTICATION_TOKEN.tokenType.eq(TokenType.valueOf(tokenType))
                        )
                        .fetchFirst());
    }

    public Optional<AuthenticationToken> getUserRelatedToken(final Long userId, final String tokenType) {
        final JPAQuery<AuthenticationToken> query = new JPAQuery<>(entityManager);
        return Optional.ofNullable(
                query.from(AUTHENTICATION_TOKEN)
                        .innerJoin(AUTHENTICATION_TOKEN.user, USER)
                        .where(
                                USER.userId.eq(userId),
                                AUTHENTICATION_TOKEN.tokenType.eq(TokenType.valueOf(tokenType))
                        )
                        .fetchFirst());
    }
}
