package com.surveycraft.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configurar CORS primero
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Desactivar CSRF
                .csrf(csrf -> csrf.disable())

                // 3. Configurar rutas públicas y privadas
                .authorizeHttpRequests(auth -> auth
                        // -- Zona Pública --
                        .requestMatchers("/api/auth/**").permitAll() // Login y Registro
                        .requestMatchers(HttpMethod.GET, "/api/games/**").permitAll() // Ver juegos
                        .requestMatchers(HttpMethod.GET, "/api/surveys/**").permitAll() // Ver encuestas/resultados

                        // -- Zona Crafters (Crear cosas) --
                        .requestMatchers(HttpMethod.POST, "/api/surveys").hasRole("CRAFTER")
                        .requestMatchers(HttpMethod.POST, "/api/games").hasRole("CRAFTER") // O Admin

                        // -- Zona Players/Crafters (Responder) --
                        .requestMatchers(HttpMethod.POST, "/api/surveys/*/respond").hasAnyRole("PLAYER", "CRAFTER")

                        // -- Todo lo demás requiere autenticación --
                        .anyRequest().authenticated())

                // 4. Manejo de sesión sin estado (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 5. Agregar filtro personalizado
                .addFilterBefore(new SimpleAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Configuración de CORS para permitir peticiones desde Postman y frontends
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // Permite todos los orígenes
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // Bean para encriptar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean para manejar la autenticación en el AuthService
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}