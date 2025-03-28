package com.example.demo.config;

import com.example.demo.repository.UserRepository;
import com.example.demo.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A Spring Security filter that processes JWT authentication for each incoming request.
 * This filter intercepts requests, extracts JWT tokens from the Authorization header,
 * validates them, and sets up the Spring Security context if the token is valid.
 *
 * <p>Extends {@link OncePerRequestFilter} to ensure a single execution per request.
 *
 * @see OncePerRequestFilter
 * @see JwtUtils
 * @see UserRepository
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    /**
     * Constructs a new JwtAuthenticationFilter with the required dependencies.
     *
     * @param jwtUtils Utility class for JWT operations
     * @param userRepository Repository for user data access
     */
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepository) {
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
    }

    /**
     * Processes each incoming request to check for JWT authentication.
     *
     * <p>This method:
     * <ol>
     *   <li>Extracts the JWT token from the Authorization header if present</li>
     *   <li>Validates the token and extracts the username</li>
     *   <li>Loads user details from the repository if token is valid</li>
     *   <li>Sets up Spring Security authentication in the context if validation succeeds</li>
     * </ol>
     *
     * @param request The incoming HTTP request
     * @param response The HTTP response
     * @param filterChain The filter chain to continue processing
     * @throws ServletException if a servlet-related error occurs
     * @throws IOException if an I/O error occurs
     * @throws UsernameNotFoundException if the user from the token isn't found in the repository
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtils.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userRepository.findByUsername(username)
                    .map(user -> User.withUsername(user.getUsername())
                            .password(user.getPassword())
                            .roles(user.getRole())
                            .build())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (jwtUtils.validateToken(jwt, userDetails)) {
                var authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}