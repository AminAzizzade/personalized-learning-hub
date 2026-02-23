package com.personalized_learning_hub.config;

import com.personalized_learning_hub.entity.User;
import com.personalized_learning_hub.enums.Role;
import com.personalized_learning_hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String email = "admin@gmail.com";

        if (!userRepository.existsByEmail(email)) {
            User admin = new User();
            admin.setEmail(email);
            admin.setFullName("Admin");
            admin.setRole(Role.ADMIN);
            admin.setPassword(passwordEncoder.encode("123456"));

            userRepository.save(admin);
            System.out.println("✅ Admin kullanıcısı başarıyla oluşturuldu.");
        } else {
            System.out.println("ℹ️ Admin kullanıcısı zaten mevcut.");
        }
    }
}
