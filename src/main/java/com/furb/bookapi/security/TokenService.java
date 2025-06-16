package com.furb.bookapi.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.furb.bookapi.model.user.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service // Indica que é um serviço gerenciado pelo Spring
public class TokenService {

	@Value("${api.security.token.secret}") // Injeta a chave secreta do application.properties
	private String secret;

	/**
     * Gera um token JWT para o usuário
     * @param user Objeto do usuário autenticado
     * @return Token JWT assinado
     * @throws RuntimeException Se falhar na geração do token
     */
	public String gerenateToken(User user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create().withIssuer("auth-api").withSubject(user.getUsername())
					.withExpiresAt(generateExpirationDate()).sign(algorithm);
			return token;
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar a token: ", exception);
		}
	}

	/**
     * Valida um token JWT
     * @param token Token a ser validado
     * @return Username do subject se o token for válido
     * @throws RuntimeException Se o token for inválido ou expirado
     */
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
		} catch (TokenExpiredException exception) {
			throw new RuntimeException("Token expirado!", exception);
		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao validar o token: ", exception);
		}
	}

	/**
     * Gera data de expiração (5 minutos no futuro)
     * @return Instant com a data/hora de expiração
     */
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusMinutes(5).toInstant(ZoneOffset.of("-03:00"));
	}
}
