package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for initializing basic user data in the database.
 *
 * <p>This class provides a Spring Bean that runs at application startup
 * to ensure the existence of default user accounts if they don't already exist.
 *
 * <p>The initialized users include:
 * <ul>
 *   <li>A standard user with username "user" and password "password"</li>
 *   <li>An admin user with username "admin" and password "admin"</li>
 * </ul>
 *
 * <p>Passwords are securely encoded using the configured {@link PasswordEncoder}.
 *
 * @see CommandLineRunner
 * @see Configuration
 * @see PasswordEncoder
 */
@Configuration
public class DataInitializer {

    /**
     * Creates a {@link CommandLineRunner} bean that initializes default user accounts.
     *
     * <p>The runner checks if the default users exist in the database and creates them
     * with encoded passwords if they don't. This ensures the application always has
     * basic user accounts available after startup.
     *
     * @param userRepository the user repository for database operations
     * @param passwordEncoder the password encoder for securing user passwords
     * @return a CommandLineRunner that performs the initialization
     *
     * @see User
     * @see UserRepository
     */
    @Bean
    public CommandLineRunner initUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("password"));
                user.setRole("USER");
                userRepository.save(user);
            }

            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
            }
        };
    }
}