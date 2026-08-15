package com.example.telco_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/ventas/pendientes")
                        .hasAnyRole("BACKOFFICE", "ADMIN")
                        .requestMatchers("/ventas/*/aprobar")
                        .hasAnyRole("BACKOFFICE", "ADMIN")
                        .requestMatchers("/ventas/*/rechazar")
                        .hasAnyRole("BACKOFFICE", "ADMIN")
                        .requestMatchers("/ventas/equipo")
                        .hasAnyRole("SUPERVISOR", "ADMIN")
                        .requestMatchers("/reportes/resumen")
                        .hasAnyRole("SUPERVISOR", "ADMIN")
                        .anyRequest().authenticated())

                .formLogin(form -> form.disable())

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                .httpBasic(httpBasic -> httpBasic.disable());
        return http.build();
    }
}
