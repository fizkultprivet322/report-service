package com.example.demo.controller;

import com.example.demo.utils.JwtUtils;
import com.example.demo.dto.AuthRequestDto;
import com.example.demo.dto.AuthResponseDto;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "API for user authentication")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Operation(
            summary = "Authenticate user",
            description = "Validates user credentials and returns JWT token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication",
                            content = @Content(schema = @Schema(implementation = AuthResponseDto.class))),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content)
            })

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
     * @see AuthRequestDto
     * @see AuthResponseDto
     * @see UsernamePasswordAuthenticationToken
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody AuthRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        final UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtUtils.generateToken(user);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}