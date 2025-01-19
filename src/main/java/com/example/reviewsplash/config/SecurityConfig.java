package com.example.reviewsplash.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.reviewsplash.component.JwtAuthenticationFilter;
import com.example.reviewsplash.component.JwtTokenManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenManager jwtTokenManager;

    public SecurityConfig(JwtTokenManager jwtTokenManager) {
        this.jwtTokenManager = jwtTokenManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> publicUrls = List.of(
            "/api/users/register", 
            "/api/users/check-email", 
            "/api/users/login", 
            "/api/users/find-id", 
            "/api/users/send-auth-email",
            "/api/users/verify-email",
            "/api/users/send-auth-email-userid",
            "/api/users/update-password"
        );

        http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenManager), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(publicUrls.toArray(String[]::new)).permitAll()
                    .anyRequest().authenticated()
            )
            .formLogin(formLogin ->
                formLogin
                    .disable()
            )
            .logout(logout ->
                logout
                    .disable()
            );

        return http.build();
    }
}
