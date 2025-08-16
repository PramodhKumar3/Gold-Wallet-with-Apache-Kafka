package com.cg.gold.security;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String SECRET = "mysecretkeymysecretkeymysecretkey12";
	// private final Key key =
	// Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

	private final Key key = Keys.hmacShaKeyFor(SECRET.getBytes()); // SAFELY INIT KEY

	public String generateToken(UserDetails userDetails) {
		System.out.println("Generating token with authorities: " + userDetails.getAuthorities());

		return Jwts.builder().setSubject(userDetails.getUsername())
				.claim("authorities",
						userDetails.getAuthorities().stream()
								.map(grantedAuthority -> Map.of("authority", grantedAuthority.getAuthority()))
								.collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public Key getKey() {
		return this.key;
	}

	public String extractUsername(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	private boolean isTokenExpired(String token) {
		Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody()
				.getExpiration();
		return expiration != null && expiration.before(new Date());
	}

}
