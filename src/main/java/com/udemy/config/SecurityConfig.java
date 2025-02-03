package com.udemy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Clase de configuración para la seguridad en una aplicación Spring Boot.
@Configuration
@EnableWebSecurity // Habilita las características de seguridad web de Spring Security.
public class SecurityConfig {

    // Constructor vacío, no realiza ninguna acción.
    public SecurityConfig() {}

    // Define la configuración de la cadena de filtros de seguridad (SecurityFilterChain).
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desactiva la protección CSRF (Cross-Site Request Forgery) ya que la aplicación usa autenticación JWT.
                .csrf(customizer -> customizer.disable())

                // Habilita la autenticación básica HTTP (útil para pruebas con herramientas como Postman).
                .httpBasic(Customizer.withDefaults())

                // Configura el manejo de sesiones como "stateless" (sin estado), ideal para APIs RESTful.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Construye y devuelve la cadena de filtros configurada.
                .build();
    }

    // Define un bean para codificar contraseñas usando BCrypt, que es un algoritmo seguro.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Instancia del codificador BCrypt.
    }

    // Define un bean para manejar autenticaciones dentro de la aplicación.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Obtiene el gestor de autenticación del contexto de Spring.
    }
}