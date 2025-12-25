package com.example.demo.config;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, 
                          PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(new Role("ADMIN"));
        }
        
        if (roleRepository.findByName("USER").isEmpty()) {
            roleRepository.save(new Role("USER"));
        }

        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").get();
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(adminRoles);
            userRepository.save(admin);
        }

        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            Role userRole = roleRepository.findByName("USER").get();
            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            
            User normalUser = new User();
            normalUser.setName("Normal User");
            normalUser.setEmail("user@example.com");
            normalUser.setPassword(passwordEncoder.encode("user123"));
            normalUser.setRoles(userRoles);
            userRepository.save(normalUser);
        }
    }
}
