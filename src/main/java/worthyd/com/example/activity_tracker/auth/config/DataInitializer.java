package worthyd.com.example.activity_tracker.auth.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import worthyd.com.example.activity_tracker.auth.model.*;
import worthyd.com.example.activity_tracker.auth.repository.*;

@Configuration
public class DataInitializer {

  @Bean
  CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return args -> {
      String adminUsername = "admin";
      String adminEmail = "admin@example.com";
      String adminPassword = "admin";

      if (!userRepository.existsByUsername(adminUsername)) {
        UserEntity admin = new UserEntity();
        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword)); // şifre encode edildi

        userRepository.save(admin);
        System.out.println("Admin user created");
      }
    };
  }
}
