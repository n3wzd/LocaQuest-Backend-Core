package com.example.locaquest.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.locaquest.component.JwtAuthenticationFilter;
import com.example.locaquest.component.JwtTokenManager;

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
            "/users/register/send-auth-mail", 
            "/template/register/accept", 
            "/users/register/check-verified",
            "/users/login", 
            "/users/update-password/send-auth-email",
            "/template/update-password/accept",
            "/users/update-password/check-verified",
            "/users/update-password",
            "/update/"
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
