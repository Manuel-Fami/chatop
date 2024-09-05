package com.openclassroom.chatop.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.openclassroom.chatop.filter.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
	 private final AuthenticationProvider authenticationProvider;
	 private final JwtAuthenticationFilter jwtAuthenticationFilter;
	 
	 public SpringSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
			 AuthenticationProvider authenticationProvider) {
		 this.authenticationProvider = authenticationProvider;
		 this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}
	 	
	@Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {     

        System.out.println(" SPRING SECURITY !!!!!");

		return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/register", "/auth/login", "/swagger-ui/*", "/v3/api-docs", "/v3/api-docs/*").permitAll() 
                    .anyRequest().authenticated()) 
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)                
                .build();        
    }
		
}
