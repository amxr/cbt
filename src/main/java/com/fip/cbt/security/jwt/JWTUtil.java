package com.fip.cbt.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;

    public JWTToken generateTokens(String email) throws IllegalArgumentException, JWTCreationException {
        String accessToken = JWT.create()
                .withSubject("User Details")
                .withIssuer("com.fip.cbt")
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(secret));

        String refreshToken = JWT.create()
                .withSubject("User Details")
                .withIssuer("com.fip.cbt")
                .withExpiresAt(new Date(System.currentTimeMillis() + 1440 * 60 * 1000))
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(secret));

        return new JWTToken().setAccessToken(accessToken).setRefreshToken(refreshToken);
    }

    public String validateTokenAndRetrieveSubject(String token)throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("com.fip.cb")
                .build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("email").asString();
    }
}
