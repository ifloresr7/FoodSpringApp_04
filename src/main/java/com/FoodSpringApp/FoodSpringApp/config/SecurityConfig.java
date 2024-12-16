package com.FoodSpringApp.FoodSpringApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ServletListenerRegistrationBean<ActiveUserSessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(new ActiveUserSessionListener());
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/api/**")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
            .authorizeHttpRequests((requests) -> requests
                    .requestMatchers(
                            "/",
                            "/vehiculos",
                            "/login",
                            "/logout",
                            "/registro",
                            "/css/**", "/js/**", "/images/**",
                            "/api/usuarios/**",
                            "/api/alquileres/**",
                            "/api/vehiculos/**",
                            "/swagger-ui/**", "/v3/api-docs/**"
                    )
                    .permitAll()
                    .requestMatchers(
                            "/mis-alquileres",
                            "/mi-perfil"
                    )
                    .authenticated()
                    .requestMatchers(
                            "/gestion-vehiculos",
                            "/gestion-usuarios",
                            "/gestion-alquileres")
                    .hasRole("ADMIN"))
            .formLogin((form) -> form
                    .loginPage("/login")
                    .usernameParameter("dni")
                    .passwordParameter("password")
                    .permitAll()
                    .failureUrl("/login?error=true")
                    .defaultSuccessUrl("/", true)
                    .successHandler(successHandler))
            .logout((logout) -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll());
        return http.build();
    }
}
