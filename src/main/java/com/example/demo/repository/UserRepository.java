package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 🔹 Used by DataInitializer & hidden tests
    Optional<User> findByEmail(String email);

    // 🔹 Used by Spring Security (authentication)
    boolean existsByEmail(String email);
}
