package com.FoodSpringApp.FoodSpringApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/api/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            )
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/vehiculos", "/login", "/logout", "/registro", "/css/**", "/js/**", "/images/**", "/webjars/**", "/api/**").permitAll()
                .requestMatchers("/mis-alquileres", "/mi-perfil").authenticated()
                .requestMatchers("/gestion-vehiculos","/gestion-usuarios","/gestion-alquileres").hasRole("ADMIN")
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .usernameParameter("dni")
                .passwordParameter("password")
                .permitAll()
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/", true)
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );
        return http.build();
    }
}