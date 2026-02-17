package com.librarysystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura el acceso a los endpoints:
     * - GET (lectura) permitido sin autenticación
     * - POST/PUT/DELETE (escritura) requieren autenticación
     * - "/" redirige a "/api/v1/books" sin requerir auth
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            )
            .authorizeHttpRequests(authz -> authz
                // Permitir GET (lectura) sin autenticación
                .requestMatchers(HttpMethod.GET, "/").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/books").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/books/**").permitAll()
                
                // Permitir acceso a H2 Console (solo desarrollo)
                .requestMatchers("/h2-console/**").permitAll()
                
                // Proteger POST/PUT/DELETE (escritura)
                .requestMatchers(HttpMethod.POST, "/api/v1/books").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/v1/books/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/v1/books/**").authenticated()
                
                // Cualquier otra solicitud requiere autenticación
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())); // Necesario para H2 Console

        return http.build();
    }

    /**
     * Configura el proveedor de usuarios en memoria.
     * En producción, usar una base de datos real (UserRepository).
     */
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails adminUser = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("ADMIN")
            .build();

        UserDetails normalUser = User.builder()
            .username("user")
            .password(passwordEncoder().encode("password123"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(adminUser, normalUser);
    }

    /**
     * Encoder de contraseñas con BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
