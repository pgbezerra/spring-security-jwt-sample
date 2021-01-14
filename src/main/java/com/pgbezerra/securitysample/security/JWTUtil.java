package com.pgbezerra.securitysample.security;

import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JWTUtil {
	
	
	@Value("${jwt.expiration}")
	private Long expiration;
	@Value("${jwt.secret}")
	private String secret;
	
	public String generateToken(String email) {
		Algorithm algorithm = Algorithm.HMAC512(secret);
		String token = JWT.create()
				.withSubject(email)
				.withExpiresAt(new Date(System.currentTimeMillis() + expiration))
				.withIssuer("auth0")
				.sign(algorithm);
		return token;
	}

	public boolean isValidToken(String token) {
		DecodedJWT claims = getDecodedJWT(token);
		
		if(Objects.nonNull(claims)) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiresAt();
			Date now = new Date(System.currentTimeMillis());
			if(Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate)) {
				return true;
			}
		}
		return false;
	}

	public String getUsername(String token) {
		DecodedJWT claims = getDecodedJWT(token);
		if(Objects.nonNull(claims))
			return claims.getSubject();
		return null;
	}
	
	private DecodedJWT getDecodedJWT(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			JWTVerifier verifier = JWT.require(algorithm)
					.withIssuer("auth0")
					.build();
			return verifier.verify(token);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

} 
