package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.function.Function;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    @Override
    @SuppressWarnings("unchecked")
    default <S extends Role> S save(S entity) {

        if (entity instanceof Function<?, ?> fn) {
            Role real = new Role();
            real = ((Function<Role, Role>) fn).apply(real);
            return (S) JpaRepository.super.save(real);
        }

        return JpaRepository.super.save(entity);
    }
}
