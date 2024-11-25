package com.example.todowithcouchbase.auth.service.impl;

import com.example.todowithcouchbase.auth.config.TokenConfigurationParameter;
import com.example.todowithcouchbase.auth.model.Token;
import com.example.todowithcouchbase.auth.service.InvalidTokenService;
import com.example.todowithcouchbase.base.AbstractBaseServiceTest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

class TokenServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private TokenConfigurationParameter tokenConfigurationParameter;

    @Mock
    private InvalidTokenService invalidTokenService;

    @Test
    void testGenerateTokenWithoutRefreshToken() throws Exception {

        // Given
        Map<String, Object> claims = new HashMap<>();
        claims.put("USER_ID", "12345");

        // Generate a valid RSA key pair for testing
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();

        Mockito.when(tokenConfigurationParameter.getAccessTokenExpireMinute()).thenReturn(60);
        Mockito.when(tokenConfigurationParameter.getRefreshTokenExpireDay()).thenReturn(7);
        Mockito.when(tokenConfigurationParameter.getPrivateKey()).thenReturn(privateKey);
        Mockito.when(tokenConfigurationParameter.getIssuer()).thenReturn("issuer");

        // When
        Token token = tokenService.generateToken(claims);

        assertNotNull(token, "Token should not be null");
        assertNotNull(token.getAccessToken(), "Access token should not be null");
        assertNotNull(token.getRefreshToken(), "Refresh token should not be null");

        // Verify
        Mockito.verify(tokenConfigurationParameter).getAccessTokenExpireMinute();
        Mockito.verify(tokenConfigurationParameter).getRefreshTokenExpireDay();
        Mockito.verify(tokenConfigurationParameter, Mockito.times(2)).getPrivateKey();

    }

    @Test
    void testGenerateTokenWithRefreshToken() {

        // Given
        KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String refreshToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000))
                .signWith(privateKey) // Use the private key for signing
                .compact();

        Map<String, Object> claims = new HashMap<>();
        claims.put("USER_ID", "12345");

        // Mock the TokenConfigurationParameter to return the keys
        Mockito.when(tokenConfigurationParameter.getPrivateKey()).thenReturn(privateKey);
        Mockito.when(tokenConfigurationParameter.getPublicKey()).thenReturn(publicKey);
        Mockito.when(tokenConfigurationParameter.getAccessTokenExpireMinute()).thenReturn(60);
        Mockito.when(tokenConfigurationParameter.getIssuer()).thenReturn("issuer");
        Mockito.doNothing().when(invalidTokenService).checkForInvalidityOfToken(anyString());


        // When
        Token token = tokenService.generateToken(claims, refreshToken);

        // Then
        assertNotNull(token);
        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
        assertNotNull(token.getAccessTokenExpiresAt());

        // Verify
        Mockito.verify(tokenConfigurationParameter).getAccessTokenExpireMinute();
        Mockito.verify(invalidTokenService).checkForInvalidityOfToken(anyString());

    }


}