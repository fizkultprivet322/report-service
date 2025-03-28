package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for {@link User} entity operations.
 * <p>
 * Provides data access methods for user management, including standard CRUD operations
 * and custom queries for user authentication purposes. Uses {@link Long} as the
 * identifier type for user entities.
 *
 * <p>Key features:
 * <ul>
 *   <li>Inherits standard JPA repository operations from {@link JpaRepository}</li>
 *   <li>Provides username-based lookup for authentication</li>
 *   <li>Returns {@link Optional} for safe handling of missing users</li>
 * </ul>
 *
 * @see JpaRepository
 * @see User
 * @see Optional
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique username.
     * <p>
     * This method is primarily used during authentication to locate a user
     * by their login credentials. The {@link Optional} return type safely
     * handles cases where no user exists with the given username.
     *
     * @param username the unique username to search for
     * @return an {@link Optional} containing the matching user if found,
     *         or empty {@link Optional} if no user exists with the given username
     *
     * @throws IllegalArgumentException if the username is null
     */
    Optional<User> findByUsername(String username);
}