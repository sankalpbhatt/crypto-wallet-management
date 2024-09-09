package com.crypto.user.repository;

import com.crypto.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUserId(String userId);
    boolean existsByEmail(String email);
}
