package com.crypto.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.crypto.exception.MyServiceException;
import com.crypto.exception.model.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Value("${publicKey}")
    private String pubKey;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (StringUtils.isEmpty(header)) {
            throw new MyServiceException("Authorization header missing", ErrorCode.PERMISSION);
        }
        String jwtToken = header.substring(7);
        if (StringUtils.isEmpty(jwtToken)) {
            throw new MyServiceException("Authorization jwtToken", ErrorCode.PERMISSION);
        }
        try {
            decodeJWTToken(jwtToken);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }

    private void decodeJWTToken(String jwtToken) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(pubKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);

        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) publicKey, null);

        DecodedJWT decodedJWT = JWT.require(algorithm)
                .build()
                .verify(jwtToken);
        System.out.println("Decoded JWT:");
        System.out.println("Client ID: " + decodedJWT.getClaim("clientId").asString());
    }
}
