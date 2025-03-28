package com.example.demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for authentication responses.
 * <p>
 * Contains the JSON Web Token (JWT) generated after successful authentication.
 * This token should be included in the Authorization header of subsequent requests
 * as a Bearer token for authenticated access to protected resources.
 *
 * <p>Example response:
 * <pre>
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 * }
 * </pre>
 *
 * @see Data
 */
@Data
@Schema(description = "Authentication response containing JWT token")
public class AuthResponse {
    /**
     * The JWT token string generated after successful authentication.
     * <p>
     * This token typically contains:
     * <ul>
     *   <li>User identity claims</li>
     *   <li>Expiration timestamp</li>
     *   <li>Digital signature</li>
     * </ul>
     *
     * <p>Should be included in requests as:
     * <pre>Authorization: Bearer {token}</pre>
     */
    @Schema(
            description = "JWT token",
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private final String token;

    /**
     * Constructs a new authentication response with the provided JWT token.
     *
     * @param token the JWT token string to include in the response
     */
    public AuthResponse(String token) {
        this.token = token;
    }
}