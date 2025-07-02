package dev.rodrilang.tennis_tournaments.security;

import dev.rodrilang.tennis_tournaments.models.Credential;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    public String generateToken(Credential credential) {
        return Jwts.builder()
                .subject(credential.getUsername())
                .claim("role", credential.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getSignatureKey())
                .compact();
    }

    public Boolean isValidToken(String token, UserDetails userDetails) {
        try {
            String usernameFromToken = extractUsername(token);
            return usernameFromToken.equals(userDetails.getUsername()) &&
                    !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    private SecretKey getSignatureKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }


    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject(); // El "sub" del JWT es el username
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignatureKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
