package com.FoodSpringApp.FoodSpringApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;

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
                            "/api/usuarios/public/**",
                            "/api/alquileres/public/**",
                            "/api/vehiculos/public/**"
                    )
                    .permitAll()
                    .requestMatchers(
                            "/mis-alquileres",
                            "/mi-perfil",
                            "/api/usuarios/private/**",
                            "/api/alquileres/private/**",
                            "/api/vehiculos/private/**"
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
