package com.pgbezerra.securitysample.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.pgbezerra.securitysample.model.entity.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Slf4j
@Component
public class JWTUtil {
	
	
	@Value("${jwt.expiration}")
	private Long expiration;
	@Value("${jwt.secret}")
	private String secret;

	@SneakyThrows
	public String generateToken(User user) {
		JWTClaimsSet jwtClaimSet = createJWTClaimSet(user);

		var keyPair = generateKeyPair();

		log.info("Building JWK from the RSA Keys");

		var rsaKey = new RSAKey.Builder((RSAPublicKey) keyPair
				.getPublic())
				.keyID(UUID.randomUUID().toString()).build();

		var signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
				.jwk(rsaKey)
				.type(JOSEObjectType.JWT)
				.build(), jwtClaimSet);

		var signer = new RSASSASigner(keyPair.getPrivate());
		signedJWT.sign(signer);

		return encryptToken(signedJWT);
	}

	public boolean isValidToken(String token) {
		try {
			String decryptedToken = decryptToken(token);
			validateTokenSignature(decryptedToken);
			return true;
		} catch (Exception e){
			return false;
		}
	}

	@SneakyThrows
	public String getUsername(String token) {
		if(isValidToken(token)) {
			SignedJWT signedJWT = SignedJWT.parse(decryptToken(token));
			return signedJWT.getJWTClaimsSet().getSubject();
		}
		return null;
	}

	private JWTClaimsSet createJWTClaimSet(User user) {
		log.info("Creating the JwtClaimsSet Object for {}", user);
		return new JWTClaimsSet.Builder().subject(user.getUsername())
				.claim("authorities", user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(toList()))
				.claim("userId", user.getId())
				.issuer("http://pbjbz.com")
				.issueTime(new Date())
				.expirationTime(new Date(System.currentTimeMillis() + expiration))
				.build();
	}

	public String encryptToken(SignedJWT signedJWT) throws JOSEException {
		log.info("Starting the encryptToken method");
		var directEncryptor = new DirectEncrypter(secret.getBytes());

		JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
				.contentType("JWT")
				.build(), new Payload(signedJWT));
		log.info("Encrypting token with system's private key");

		jweObject.encrypt(directEncryptor);

		log.info("Token encrypted");

		return jweObject.serialize();
	}

	@SneakyThrows
	private KeyPair generateKeyPair(){
		log.info("Generating RSA 2048 bits Keys");
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);
		return generator.genKeyPair();
	}

	@SneakyThrows
	public String decryptToken(String encryptedToken) {
		log.info("Decrypting token");
		var jweObject = JWEObject.parse(encryptedToken);
		var directDecrypter = new DirectDecrypter(secret.getBytes());
		jweObject.decrypt(directDecrypter);
		log.info("Token decrypted, returning signed token");
		return jweObject.getPayload().toSignedJWT().serialize();
	}

	@SneakyThrows
	public void validateTokenSignature(String signedToken){
		log.info("Starting method to validate token signature...");
		var signedJWT = SignedJWT.parse(signedToken);
		log.info("Token parsed! Retrieving public key from signed token");

		RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());

		log.info("Public key retrieved, validating signature");

		if(!signedJWT.verify(new RSASSAVerifier(publicKey)))
			throw new AccessDeniedException("Invalid token signature!");

		log.info("The token has a valid signature");
	}

} 
