package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Authentication request")
public class AuthRequest {
    /**
     * The username credential for authentication.
     * <p>
     * Typically matches the unique username stored in the system.
     */
    @Schema(
            description = "Username",
            example = "user",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * The password credential for authentication.
     * <p>
     * Should match the user's stored password (after encoding).
     * <p>
     * Note: In production, passwords should always be transmitted over secure channels (HTTPS).
     */
    @Schema(
            description = "Password",
            example = "password",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}