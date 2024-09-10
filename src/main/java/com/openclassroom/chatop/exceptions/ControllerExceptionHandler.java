package com.openclassroom.chatop.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    // Taille trop grosse picture
    @ExceptionHandler(MaxUploadSizeExceededException.class)
	    public ResponseEntity<Map<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
	        Map<String, Object> response = new HashMap<>();
	        response.put("status", HttpStatus.BAD_REQUEST);
	        response.put("message", "File size exceeds the configured maximum");

	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Si certaines parties d'une requêtes multipart sont manquantes
    @ExceptionHandler(MissingServletRequestPartException.class)
	 public ResponseEntity<Map<String, String>> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
	        Map<String, String> response = new HashMap<>();
	        response.put("error", "Un paramètre requis est manquant");
	        response.put("part", ex.getRequestPartName());
	        response.put("message", ex.getMessage());

	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}


    // Expiration Jwt Token
    @ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                             .body("Token has expired.");
	}

    //Invalid Token
    @ExceptionHandler({ SignatureException.class, MalformedJwtException.class })
	public ResponseEntity<String> handleSignatureException(Exception ex) {
	      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
	                        .body("Invalid JWT");
    }

    // Gérer les exceptions ResponseStatusException / conflit
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ex.getReason());
    }

    // Si il manque un parametre dans le body 
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleInvalidArgument(MethodArgumentNotValidException ex) {
        
        Map<String, Object> errorMap = new HashMap<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorMap.put(error.getField(), error.getDefaultMessage());
        }
        
        errorMap.put("status", HttpStatus.BAD_REQUEST);
        
        return errorMap;
    }

    // Si il n'y a pas de body alors que attendu 
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String errorMessage = "Invalid request body: " + e.getLocalizedMessage();
    	 
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);        
    }


    // 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequestException(IllegalArgumentException ex) {
        return new ResponseEntity<>("Bad Request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 401 Unauthorized
    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
    public ResponseEntity<Object> handleUnauthorizedException(org.springframework.security.core.AuthenticationException ex) {
        return new ResponseEntity<>("Unauthorized: " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 403 Forbidden
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Object> handleForbiddenException(org.springframework.security.access.AccessDeniedException ex) {
        return new ResponseEntity<>("Forbidden: " + ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    // 500 Internal Server Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleInternalServerError(Exception ex) {
        return new ResponseEntity<>("Internal Server Error: An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}
