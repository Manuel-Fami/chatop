package com.openclassroom.chatop.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.openclassroom.chatop.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    
    public JwtAuthenticationFilter(JwtService jwtService, 
    		UserDetailsService userDetailsService, 
    		HandlerExceptionResolver handlerExceptionResolver) {
            this.jwtService = jwtService;
            this.userDetailsService = userDetailsService;
            this.handlerExceptionResolver = handlerExceptionResolver;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		System.out.println("START FILTER !!!");

		// Récupération de l'en-tête Authorization
		String authHeader = request.getHeader("Authorization");
		
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}
		 
		try {
			// Extraction et validation du JWT
			String jwt = authHeader.substring(7);
			String userEmail = jwtService.extractUsername(jwt);
			
			// Est ce que l'utilsateur est déjà authentifié ?
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			// Pas encore authentifié
			if (userEmail != null && authentication == null) {
				// Chargement de l'utilisateur
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

				// Vérification du token
				if (jwtService.isTokenValid(jwt, userDetails)) {
					// Création d'une interface d'authentification par Spring Security
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
							userDetails,
							null,
							userDetails.getAuthorities()
					);

					// Ajoute le header "Bearer"
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					// Stockage de l'authentification par Spring Security
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			filterChain.doFilter(request, response);

		} catch (Exception exception) {
			// Gestion des exceptions
			exception.printStackTrace();
			handlerExceptionResolver.resolveException(request, response, null, exception);
		}
			
		System.out.println("END FILTER !!!");
	}
    
    
    
    
}