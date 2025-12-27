package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            // ----- ROLES -----
            Role adminRole = roleRepository
                    .findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            Role userRole = roleRepository
                    .findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));

            // ----- ADMIN USER -----
            if (!userRepository.existsByEmail("admin@test.com")) {
                User admin = new User();
                admin.setEmail("admin@test.com");
                admin.setName("Admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.getRoles().add(adminRole);
                userRepository.save(admin);
            }

            // ----- NORMAL USER -----
            if (!userRepository.existsByEmail("user@test.com")) {
                User user = new User();
                user.setEmail("user@test.com");
                user.setName("User");
                user.setPassword(passwordEncoder.encode("user123"));
                user.getRoles().add(userRole);
                userRepository.save(user);
            }
        };
    }
}
