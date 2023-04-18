package com.gymbuddy.auth.persistence.repository;

import com.gymbuddy.auth.persistence.domain.User;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository class for User entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long>, QuerydslPredicateExecutor<User> {
}

