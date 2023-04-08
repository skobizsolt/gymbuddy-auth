package com.gymbuddy.auth.persistence.repository;

import com.gymbuddy.auth.persistence.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getUsersByUserId(final Long userId);

    Optional<User> getUsersByEmail(final String email);

    Optional<User> getUsersByUsername(final String username);
}

