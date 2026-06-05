package com.gymApp.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactivamos CSRF temporalmente para que Postman y Android puedan mandar datos POST sin problemas
                .csrf(AbstractHttpConfigurer::disable)
                // Configuramos las reglas de las rutas
                .authorizeHttpRequests(auth -> auth
                        // Liberamos (permitimos a todos) cualquier ruta que empiece con /api/auth/
                        .requestMatchers("/api/auth/**", "/api/admin/**", "/api/usuarios/**").permitAll()
                        // Cualquier otra ruta de la app va a requerir estar logueado
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}