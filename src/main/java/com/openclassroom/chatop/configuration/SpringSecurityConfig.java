package com.openclassroom.chatop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

import com.openclassroom.chatop.services.CustomUserDetailsService;
import com.openclassroom.chatop.services.JwtService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private static final String[] WHITE_LIST_SWAGGER_URL = {
        "/api-docs",
        "/v3/api-docs/**",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/swagger-ui/index.html",
        "/swagger-ui/index.html/**"
    };

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public SpringSecurityConfig(JwtService jwtService, CustomUserDetailsService customUserDetailsService, AuthenticationConfiguration authenticationConfiguration) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(http), jwtService);
        // Définit l'URL à traiter par le filtre d'authentification JWT.
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/login", "/api/auth/register", "/api/me", "/api/auth/verify").permitAll()
                .requestMatchers(WHITE_LIST_SWAGGER_URL).permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        // Ajoute le filtre d'authentification JWT à la chaîne de filtres de sécurité.
        http.addFilter(jwtAuthenticationFilter);
        // Ajoute le filtre d'autorisation JWT avant le filtre d'authentification JWT dans la chaîne de filtres.
        http.addFilterBefore(new JwtAuthorizationFilter(jwtService, customUserDetailsService), JwtAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    // public AuthenticationManager authenticationManager() throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    // }
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

   
}
