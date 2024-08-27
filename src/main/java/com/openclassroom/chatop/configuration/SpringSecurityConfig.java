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
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
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
    @SuppressWarnings("unused")
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SpringSecurityConfig(JwtService jwtService, CustomUserDetailsService customUserDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationConfiguration.class).getAuthenticationManager();

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtService);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        // JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtService, customUserDetailsService, authenticationManager);

        // Définit l'URL à traiter par le filtre d'authentification JWT.
        // jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(WHITE_LIST_SWAGGER_URL).permitAll()
                .requestMatchers("/api/messages/**").authenticated()
                .requestMatchers("/api/user/**").authenticated()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            // Ajoute le filtre d'authentification JWT à la chaîne de filtres de sécurité.
            http.addFilter(jwtAuthenticationFilter);
            // Ajoute le filtre d'autorisation JWT avant le filtre d'authentification JWT dans la chaîne de filtres.
            http.addFilterBefore(new JwtAuthorizationFilter(jwtService, customUserDetailsService, authenticationManager), JwtAuthenticationFilter.class);

            
            // // Ajoute le filtre d'authentification JWT à la chaîne de filtres de sécurité.
            // http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            // // Ajoute le filtre d'autorisation JWT avant le filtre d'authentification JWT dans la chaîne de filtres.
            // .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Bean
    // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    //     return authenticationConfiguration.getAuthenticationManager();
    // }


    // public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //     auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    // }

   
}
