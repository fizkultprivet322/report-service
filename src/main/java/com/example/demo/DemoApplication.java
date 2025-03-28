package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Spring Boot application.
 * <p>
 * This class serves as the configuration and bootstrap class for the application.
 * The {@link SpringBootApplication} annotation enables:
 * <ul>
 *   <li>Autoconfiguration of Spring components</li>
 *   <li>Component scanning in the current package and sub-packages</li>
 *   <li>Embedded server configuration</li>
 * </ul>
 *
 * <p>The application can be run either:
 * <ul>
 *   <li>As a standalone Java application</li>
 *   <li>Through Spring Boot Maven plugin (spring-boot:run)</li>
 *   <li>As a packaged executable JAR/WAR</li>
 * </ul>
 *
 * @see SpringBootApplication
 */
@SpringBootApplication
public class DemoApplication {

    /**
     * Main method that serves as the application entry point.
     * <p>
     * Delegates to Spring Boot's {@link SpringApplication} to:
     * <ul>
     *   <li>Create the ApplicationContext</li>
     *   <li>Register all beans</li>
     *   <li>Start embedded servers</li>
     *   <li>Run the application</li>
     * </ul>
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}