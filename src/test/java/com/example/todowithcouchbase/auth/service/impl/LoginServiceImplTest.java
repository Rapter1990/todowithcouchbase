package com.example.todowithcouchbase.auth.service.impl;

import com.example.todowithcouchbase.auth.exception.PasswordNotValidException;
import com.example.todowithcouchbase.auth.exception.UserNotFoundException;
import com.example.todowithcouchbase.auth.model.Token;
import com.example.todowithcouchbase.auth.model.dto.request.LoginRequest;
import com.example.todowithcouchbase.auth.model.entity.UserEntity;
import com.example.todowithcouchbase.auth.repository.UserRepository;
import com.example.todowithcouchbase.auth.service.TokenService;
import com.example.todowithcouchbase.base.AbstractBaseServiceTest;
import com.example.todowithcouchbase.builder.AdminUserBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private LoginServiceImpl loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Test
    void login_ValidCredentials_ReturnsToken() {

        // Given
        final LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        final UserEntity adminEntity = new AdminUserBuilder().withValidFields().build();

        final Token expectedToken = Token.builder()
                .accessToken("mockAccessToken")
                .accessTokenExpiresAt(123456789L)
                .refreshToken("mockRefreshToken")
                .build();

        // When
        when(userRepository.findUserEntityByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(adminEntity));

        when(passwordEncoder.matches(loginRequest.getPassword(), adminEntity.getPassword()))
                .thenReturn(true);

        when(tokenService.generateToken(adminEntity.getClaims())).thenReturn(expectedToken);

        Token actualToken = loginService.login(loginRequest);

        // Then
        assertEquals(expectedToken.getAccessToken(), actualToken.getAccessToken());
        assertEquals(expectedToken.getRefreshToken(), actualToken.getRefreshToken());
        assertEquals(expectedToken.getAccessTokenExpiresAt(), actualToken.getAccessTokenExpiresAt());

        // Verify
        verify(userRepository).findUserEntityByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), adminEntity.getPassword());
        verify(tokenService).generateToken(adminEntity.getClaims());

    }

    @Test
    void login_InvalidEmail_ThrowsAdminNotFoundException() {

        // Given
        final LoginRequest loginRequest = LoginRequest.builder()
                .email("nonexistent@example.com")
                .password("password123")
                .build();

        // When
        when(userRepository.findUserEntityByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.empty());

        // Then
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> loginService.login(loginRequest));

        assertEquals("User not found!\n " + loginRequest.getEmail(), exception.getMessage());

        // Verify
        verify(userRepository).findUserEntityByEmail(loginRequest.getEmail());
        verifyNoInteractions(passwordEncoder, tokenService);

    }

    @Test
    void login_InvalidPassword_ThrowsPasswordNotValidException() {

        // Given
        final LoginRequest loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("invalidPassword")
                .build();

        final UserEntity adminEntity = UserEntity.builder()
                .email(loginRequest.getEmail())
                .password("encodedPassword")
                .build();

        // When
        when(userRepository.findUserEntityByEmail(loginRequest.getEmail()))
                .thenReturn(Optional.of(adminEntity));

        when(passwordEncoder.matches(loginRequest.getPassword(), adminEntity.getPassword()))
                .thenReturn(false);

        // Then
        PasswordNotValidException exception = assertThrows(PasswordNotValidException.class,
                () -> loginService.login(loginRequest));

        assertNotNull(exception);

        // Verify
        verify(userRepository).findUserEntityByEmail(loginRequest.getEmail());
        verify(passwordEncoder).matches(loginRequest.getPassword(), adminEntity.getPassword());
        verifyNoInteractions(tokenService);

    }

}