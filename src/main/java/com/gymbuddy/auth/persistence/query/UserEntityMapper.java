package com.gymbuddy.auth.persistence.query;

import com.gymbuddy.auth.persistence.domain.QUser;
import com.gymbuddy.auth.persistence.domain.User;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Query class for User entity.
 */
@Component
@AllArgsConstructor
public class UserEntityMapper {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QUser USER = QUser.user;

    public Optional<User> getUsersByUserId(final Long userId) {
        final JPAQuery<User> query = new JPAQuery<>(entityManager);
        return Optional.ofNullable(
                query.from(USER)
                        .where(USER.userId.eq(userId))
                        .fetchFirst()
        );
    }

    public Optional<User> getUsersByEmail(final String email) {
        final JPAQuery<User> query = new JPAQuery<>(entityManager);
        return Optional.ofNullable(
                query.from(USER)
                        .where(USER.email.eq(email))
                        .fetchFirst()
        );
    }

    public Optional<User> getUsersByUsername(final String username) {
        final JPAQuery<User> query = new JPAQuery<>(entityManager);
        return Optional.ofNullable(
                query.from(USER)
                        .where(USER.username.eq(username))
                        .fetchFirst()
        );
    }
}
