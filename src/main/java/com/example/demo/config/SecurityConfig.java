package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Main security configuration class for the application.
 *
 * <p>This class configures:
 * <ul>
 *   <li>HTTP security rules and endpoint authorization</li>
 *   <li>JWT authentication filter integration</li>
 *   <li>User details service configuration</li>
 *   <li>Authentication manager setup</li>
 *   <li>Password encoding strategy</li>
 * </ul>
 *
 * <p>Uses stateless session management with JWT for authentication.
 *
 * @see EnableWebSecurity
 * @see Configuration
 * @see JwtAuthenticationFilter
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserRepository userRepository;

    /**
     * Constructs a new SecurityConfig with required dependencies.
     *
     * @param jwtAuthFilter the JWT authentication filter
     * @param userRepository the user repository for user data access
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserRepository userRepository) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userRepository = userRepository;
    }

    /**
     * Configures the security filter chain with HTTP security rules.
     *
     * <p>Configuration includes:
     * <ul>
     *   <li>Disabling CSRF protection (for API using JWT)</li>
     *   <li>Setting up endpoint authorization rules:
     *     <ul>
     *       <li>Public access to login endpoint</li>
     *       <li>Role-based access to specific endpoints</li>
     *       <li>Authenticated access for all other requests</li>
     *     </ul>
     *   </li>
     *   <li>Setting stateless session policy</li>
     *   <li>Adding JWT authentication filter</li>
     * </ul>
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers("/api/reports/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configures the user details service that loads user-specific data.
     *
     * <p>The service loads users from the repository and maps them to Spring Security's
     * UserDetails implementation with proper authorities based on roles.
     *
     * @return the configured UserDetailsService
     * @throws UsernameNotFoundException if a user is not found
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .map(user -> org.springframework.security.core.userdetails.User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Exposes the AuthenticationManager as a Spring bean.
     *
     * @param config the AuthenticationConfiguration
     * @return the AuthenticationManager
     * @throws Exception if authentication manager cannot be obtained
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the password encoder to use BCrypt hashing.
     *
     * @return the PasswordEncoder implementation (BCrypt)
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}