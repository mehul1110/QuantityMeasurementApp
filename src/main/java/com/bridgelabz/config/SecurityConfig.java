package com.bridgelabz.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * UC17: Basic Security Configuration.
 *
 * For development purposes, this configuration permits ALL requests to allow
 * testing of the REST endpoints and H2 console without authentication barriers.
 *
 * Note: In a production environment, this would be replaced with JWT token
 * authentication, role-based access control, and HTTPS enforcement.
 *
 * Uses the modern Spring Security 6.x SecurityFilterChain approach
 * (WebSecurityConfigurerAdapter is deprecated in Spring Boot 3.x).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configures HTTP security rules.
     *
     * - Disables CSRF for API testing (not suitable for production with browser clients).
     * - Permits all requests — development-only configuration.
     * - Allows H2 console to display in iframes (frameOptions disabled for same origin).
     * - Allows all Swagger UI, API docs, actuator, and API requests.
     *
     * @param http Spring HttpSecurity builder
     * @return configured SecurityFilterChain
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**", "/api/**")
                .disable()
            )
            .authorizeHttpRequests(auth -> auth
                // H2 Console
                .requestMatchers("/h2-console/**").permitAll()
                // API endpoints
                .requestMatchers("/api/**").permitAll()
                // Swagger UI & OpenAPI docs
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**").permitAll()
                // Actuator endpoints
                .requestMatchers("/actuator/**").permitAll()
                // All other requests
                .anyRequest().permitAll()
            )
            // Allow H2 console to render in iframes (same origin)
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );

        return http.build();
    }

    /**
     * Provides BCryptPasswordEncoder bean for future authentication use.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
