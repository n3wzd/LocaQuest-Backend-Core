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

import com.example.locaquest.component.TokenComponent;
import com.example.locaquest.constant.Route;
import com.example.locaquest.util.AuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenComponent tokenComponent;

    public SecurityConfig(TokenComponent tokenComponent) {
        this.tokenComponent = tokenComponent;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        List<String> publicUrls = List.of(
        	Route.USER + Route.USER_REGISTER_MAIL, 
            Route.USER + Route.USER_REGISTER_VERIFIED,
            Route.USER + Route.USER_LOGIN, 
            Route.TEMPLATE + Route.TEMPLATE_UPDATE_PASSWORD_ACCREPT,
            Route.TEMPLATE + Route.TEMPLATE_REGISTER_ACCREPT,
            Route.USER + Route.USER_UPDATE_PASSWORD,
            Route.USER + Route.USER_UPDATE_PASSWORD_MAIL,
            Route.USER + Route.USER_UPDATE_PASSWORD_VERIFIED, 
            
            Route.ACTIVITY + Route.ACTIVITY_INIT,
            Route.USER_STATUS + Route.USER_STATUS_START,
            Route.USER_STATUS + Route.USER_STATUS_ACHIEVE
        );

        http
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(new AuthenticationFilter(tokenComponent), UsernamePasswordAuthenticationFilter.class)
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
