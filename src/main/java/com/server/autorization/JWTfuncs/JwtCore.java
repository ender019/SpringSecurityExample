package com.server.autorization.JWTfuncs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtCore {
    @Value("${autorization.app.secret}")
    private String secret;

    @Value("${autorization.app.lifetime}")
    private int lifetime;

    public String generateToken(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();
        byte[] bytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject((userDetails.getUsername())).issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + lifetime))
                .signWith(new SecretKeySpec(bytes, "HmacSHA256"))
                .compact();
    }

    public String generateToken(String username){
        byte[] bytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder().subject((username)).issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + lifetime))
                .signWith(new SecretKeySpec(bytes, "HmacSHA256"))
                .compact();
    }

    public String getNameFromJwt(String token) {
        byte[] bytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(new SecretKeySpec(bytes, "HmacSHA256"))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
