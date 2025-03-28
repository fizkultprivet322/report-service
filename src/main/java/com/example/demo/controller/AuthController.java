package com.example.demo.controller;

import com.example.demo.utils.JwtUtils;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling authentication operations.
 *
 * <p>Provides endpoints for user authentication and JWT token generation.
 *
 * <p>This controller is responsible for:
 * <ul>
 *   <li>Validating user credentials</li>
 *   <li>Generating JWT tokens for authenticated users</li>
 *   <li>Returning authentication responses with generated tokens</li>
 * </ul>
 *
 * @see RestController
 * @see RequestMapping
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    /**
     * Authenticates a user and generates a JWT token upon successful authentication.
     *
     * <p>The authentication process:
     * <ol>
     *   <li>Validates the provided credentials using AuthenticationManager</li>
     *   <li>Loads user details if authentication succeeds</li>
     *   <li>Generates a JWT token for the authenticated user</li>
     *   <li>Returns the token in the response</li>
     * </ol>
     *
     * @param request the authentication request containing username and password
     * @return ResponseEntity containing the generated JWT token in AuthResponse
     *
     * @throws org.springframework.security.core.AuthenticationException if authentication fails
     *
     * @see AuthRequest
     * @see AuthResponse
     * @see UsernamePasswordAuthenticationToken
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}