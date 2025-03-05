package org.example.bankmanagementsystem.config;

import org.example.bankmanagementsystem.Entities.Role;
import org.example.bankmanagementsystem.Entities.User;
import org.example.bankmanagementsystem.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminDataLoader(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Проверяем, существует ли уже админ-аккаунт по username "admin"
        if (!userRepository.existsByEmail("admin")) {
            User admin = new User();
            admin.setEmail("admin");
            // Задаём пароль "admin123" (его следует заменить и изменить при необходимости)
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ROLE_ADMIN);
            admin.setIsVerified(true);
            // Устанавливайте дополнительные поля, если необходимо
            userRepository.save(admin);
            System.out.println("Админ аккаунт успешно создан!");
        } else {
            System.out.println("Админ аккаунт уже существует.");
        }
    }
}
