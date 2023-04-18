package com.gymbuddy.auth.persistence.repository;

import com.gymbuddy.auth.persistence.domain.auth.AuthenticationToken;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for Authentication Token.
 */
@Repository
public interface TokenRepository
        extends CrudRepository<AuthenticationToken, Long>, QuerydslPredicateExecutor<AuthenticationToken> {
}
