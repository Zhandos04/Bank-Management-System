package org.example.bankmanagementsystem.repositories;

import org.example.bankmanagementsystem.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User getUserById(Long id);
    Boolean existsByEmail(String email);
}
