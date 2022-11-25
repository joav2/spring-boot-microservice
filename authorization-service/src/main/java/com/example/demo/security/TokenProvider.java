package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class TokenProvider {
	
	@Value("${app.jwt.secret}")
	private String secret;
	
	@Value("${app.jwt.expiration.minutes}")
    private Long jwtExpirationMinutes;
	
	public Jws<Claims> getClaimFromToken(String token) {
		byte[] signingKey = secret.getBytes();
        return Jwts
                .parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
	}
	
	public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token).getBody();

        return claimsResolver.apply(claims);
    }
    private Jws<Claims> getAllClaimsFromToken(String token) {
        byte[] signingKey = secret.getBytes();
        return Jwts
                .parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
    }
    private Boolean isTokenExpired(String token) {
        final Date  expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }
    public String generateToken(Authentication authentication) {
        ApplicationUserDetails customUserDetails = (ApplicationUserDetails) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, customUserDetails);
    }
    private String doGenerateToken(Map<String, Object> claims, ApplicationUserDetails subject) {

        List<String> roles = subject.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        byte[] signingKey = secret.getBytes();
        return Jwts.builder()
                .setHeaderParam("typ", TOKEN_TYPE)
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(30).toInstant()))
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setSubject(subject.getUsername())
                .claim("rol", roles)
                .claim("name", subject.getUsername())
                .claim("preferred_username", subject.getUsername())
                .claim("email", subject.getEmail())
                .compact();
    }

    public Boolean validateToken(String token, ApplicationUserDetails customUserDetails) {
        final String username = getUsernameFromToken(token);

        return (username.equals(customUserDetails.getUsername()) && !isTokenExpired(token));
	}

	public static final String TOKEN_TYPE = "JWT";
}
