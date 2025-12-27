package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // ---- Roles ----
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            // ---- Admin User ----
            User admin = userRepository.findByEmail("admin@example.com")
                    .orElseGet(() -> {
                        User u = new User();
                        u.setEmail("admin@example.com");     // ✅ FIXED
                        u.setPassword(passwordEncoder.encode("admin123"));
                        u.setRoles(Set.of(adminRole));
                        return userRepository.save(u);
                    });

            // ---- Normal User ----
            User normalUser = userRepository.findByEmail("user@example.com")
                    .orElseGet(() -> {
                        User u = new User();
                        u.setEmail("user@example.com");      // ✅ FIXED
                        u.setPassword(passwordEncoder.encode("user123"));
                        u.setRoles(Set.of(userRole));
                        return userRepository.save(u);
                    });
        };
    }
}
