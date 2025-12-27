// package com.example.demo.service.impl;
// import com.example.demo.entity.Role;
// import com.example.demo.entity.User;
// import com.example.demo.repository.RoleRepository;
// import com.example.demo.repository.UserRepository;
// import com.example.demo.service.UserService;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
// import java.util.Map;
// import java.util.Optional;

// @Service
// public class UserServiceImpl implements UserService {
//     private final UserRepository userRepository;
//     private final RoleRepository roleRepository;
//     private final PasswordEncoder encoder;

//     public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
//         this.userRepository = userRepository;
//         this.roleRepository = roleRepository;
//         this.encoder = encoder;
//     }

//     @Override
//     @Transactional
//     public User registerUser(Map<String, String> userData) {
//         String email = userData.get("email");
//         if(email==null || email.isEmpty()) throw new IllegalArgumentException("Email required");
//         if (userRepository.findByEmail(email).isPresent()) throw new IllegalArgumentException("Email already exists");

//         User user = new User();
//         user.setName(userData.get("name"));
//         user.setEmail(email);
//         user.setPassword(encoder.encode(userData.get("password")));
        
//         Optional<Role> roleOpt = roleRepository.findByName("USER");
//         Role userRole = roleOpt.orElseGet(() -> roleRepository.save(new Role("USER")));
//         user.getRoles().add(userRole);
//         return userRepository.save(user);
//     }
// }


package com.example.demo.service.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(User user) {

        // Check duplicate email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Assign USER role
        Role userRole = roleRepository.findByName("USER")
                .orElseGet(() -> roleRepository.save(new Role("USER")));

        user.getRoles().add(userRole);

        // Encode password (CRITICAL for login test)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
    }
}
