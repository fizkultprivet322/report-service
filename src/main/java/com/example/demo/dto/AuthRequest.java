package com.example.demo.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for authentication requests.
 * <p>
 * Contains the credentials required for user authentication:
 * <ul>
 *   <li>Username - unique identifier for the user</li>
 *   <li>Password - secret phrase for authentication</li>
 * </ul>
 *
 * <p>Used as request body for authentication endpoints.
 *
 * @see Data
 */
@Data
public class AuthRequest {
    /**
     * The username credential for authentication.
     * <p>
     * Typically matches the unique username stored in the system.
     */
    private String username;

    /**
     * The password credential for authentication.
     * <p>
     * Should match the user's stored password (after encoding).
     * <p>
     * Note: In production, passwords should always be transmitted over secure channels (HTTPS).
     */
    private String password;
}