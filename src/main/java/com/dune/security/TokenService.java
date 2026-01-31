package com.dune.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dune.exception.BadRequestException;
import com.dune.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("delp")
                    .withSubject(user.getEmail())
                    .withExpiresAt(getExpiredDate())
                    .sign(algorithm);

            return token;
        }catch(JWTCreationException e) {
            throw new BadRequestException("Error to generate token: " + e.getMessage());
        }
    }


    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("dunatoken")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch(JWTVerificationException e) {
            return "";
        }
    }

    private Instant getExpiredDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
