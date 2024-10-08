package com.example.springboot_jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final String SECRET_KEY = "my-secret-key";
	private final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours
	
	public String generateToken(String username) {
		return Jwts
				.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.ES256, SECRET_KEY)
				.compact();
	}
	
	public String extractUsername(String token) {
		return Jwts
				.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	private boolean isTokenExpired(String token) {
		return Jwts
				.parser()
				.setSigningKey(SECRET_KEY)
				.parseClaimsJws(token)
				.getBody()
				.getExpiration()
				.before(new Date());
	}
	
	public boolean validateToken(String token, String username) {
		String tokenUsername = extractUsername(token);
		return username.equals(tokenUsername) && !isTokenExpired(token);
	}
}
