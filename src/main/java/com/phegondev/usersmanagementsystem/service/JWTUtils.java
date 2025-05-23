package com.phegondev.usersmanagementsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.phegondev.usersmanagementsystem.entity.OurUsers;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.Objects;

@Component
public class JWTUtils {

    private SecretKey Key;
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public JWTUtils(){
        String secretString = "673567893696976453275974432697R634967R738467R678T3486576834R8763T4783876764538745673865";

        // Decode the Base64-encoded secret string into its original binary format (byte array)
        byte[] keyBytes = Base64.getDecoder().decode(secretString.getBytes(StandardCharsets.UTF_8));

        // Create a SecretKey object from the byte array using the HMAC-SHA256 algorithm
        // "SecretKeySpec" Allows you to create a SecretKey from a raw byte array, specifying the algorithm you want to use.
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    public String generateToken(UserDetails userDetails,  List<String> actionList){
        // Extract role from UserDetails (assuming your OurUsers implements UserDetails)
    	
    	   OurUsers user = (OurUsers) userDetails;
    String role = userDetails.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .map(String::toLowerCase)
            .orElse("user"); // Default to "USER" if no role is found
        return Jwts.builder()
                .subject(userDetails.getUsername()) // Identifies the user
                .claim("role", user.getRole()) // Add role claim
                .claim("actions", actionList)  
                .issuedAt(new Date(System.currentTimeMillis())) // Token creation time
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration time
                .signWith(Key) // Signs with the secret key
                .compact(); // Generates the token as a string
    }

    public String generateRefreshToken(HashMap<String, Objects> claims, UserDetails userDetails){
        return Jwts.builder()
                // Claims are custom key-value pairs added to the refresh token to store additional data like roles or permissions.
                // These are included to provide extra information about the user/session without querying the database.
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        return claimsTFunction.apply
                (Jwts.parser()
                        .verifyWith(Key)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload());
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) // Compares the username from the token with the username stored in the application's UserDetails.
                && !isTokenExpired(token)); // Check if the token's expiration date has passed.
    }

    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration).before(new Date()); // new Date() => current date
        // Returns true if the expiration date is before the current date (indicating the token has expired).
        // Returns false if the expiration date is after the current date (indicating the token is still valid).
    }
}