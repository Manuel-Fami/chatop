package com.openclassroom.chatop.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;

@Service
public class JwtService {
    // private static final Logger logger = Logger.getLogger(JwtService.class.getName());

    @Value("${jwt.expiration}")
    private static long expiration;

    @Value("${jwt.secret}")
    private String secret;

    // --------------------
    private Algorithm algorithm;
    private JWTVerifier verifier;
    
    @PostConstruct
    public void init() {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
    }

    public String generateToken(UserDetails userDetails) {
        return JWT.create()
                .withSubject(userDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration * 1000)) // 1 hour
                .sign(algorithm);
    }

    public String validateToken(String token) {
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getSubject();
    }

    // --------------------


    // // Store the secret key as a static final field
    // private static final SecretKey SIGNING_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    

    // // Generate JWT token
    // public static String generateToken(String username) {
    //     String token = Jwts.builder()
    //         .setSubject(username)
    //         .setIssuedAt(new Date())
    //         .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
    //         .signWith(SIGNING_KEY, SignatureAlgorithm.HS512)
    //         .compact();
    
    //     // Log the generated token
    //     logger.info("Generated JWT: " + token);
    
    //     return token;
    // }

    //  // Extract username from token
    // public String getUsernameFromToken(String token) {
    //     return getClaimFromToken(token, Claims::getSubject);
    // }

    // // Extract expiration date from token
    // public Date getExpirationDateFromToken(String token) {
    //     return getClaimFromToken(token, Claims::getExpiration);
    // }

    // // Extract a specific claim from the token
    // public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    //     final Claims claims = getAllClaimsFromToken(token);
    //     return claimsResolver.apply(claims);
    // }

    // // Extract all claims from the token
    // public Claims getAllClaimsFromToken(String token) {
    //     try {
    //         return Jwts.parserBuilder()
    //                 .setSigningKey(SIGNING_KEY)
    //                 .build()
    //                 .parseClaimsJws(token)
    //                 .getBody();
    //     } catch (Exception e) {
    //         // Log the error if the token parsing fails
    //         logger.severe("Error parsing JWT: " + e.getMessage());
    //         throw e; // Re-throw the exception to ensure it's handled properly elsewhere
    //     }
    // }

    // // Check if the token is expired
    // private Boolean isTokenExpired(String token) {
    //     final Date expiration = getExpirationDateFromToken(token);
    //     return expiration.before(new Date());
    // }
    
    // // Validate the JWT token
    // public Boolean validateToken(String token, String username) {
    //     final String usernameFromToken = getUsernameFromToken(token);
    //     return (usernameFromToken.equals(username) && !isTokenExpired(token));
    // }
}
