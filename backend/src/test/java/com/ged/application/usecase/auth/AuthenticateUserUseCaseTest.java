package com.ged.application.usecase.auth;

import com.ged.application.dto.request.LoginRequest;
import com.ged.application.dto.response.AuthResponse;
import com.ged.domain.model.User;
import com.ged.domain.model.enums.UserRole;
import com.ged.domain.repository.UserRepository;
import com.ged.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AuthenticateUserUseCase useCase;

    private User testUser;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .password("$2a$10$hashedPassword")
                .role(UserRole.USER)
                .build();

        loginRequest = new LoginRequest("testuser", "password123");
    }

    @Test
    void execute_ValidCredentials_ReturnsAuthResponse() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(true);
        when(jwtTokenProvider.createToken(testUser)).thenReturn("jwt-token");

        AuthResponse response = useCase.execute(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("testuser", response.getUsername());
        assertEquals("USER", response.getRole());

        verify(userRepository).findByUsername("testuser");
        verify(passwordEncoder).matches("password123", testUser.getPassword());
        verify(jwtTokenProvider).createToken(testUser);
    }

    @Test
    void execute_InvalidUsername_ThrowsBadCredentialsException() {
        when(userRepository.findByUsername("invaliduser")).thenReturn(Optional.empty());

        LoginRequest invalidRequest = new LoginRequest("invaliduser", "password123");

        assertThrows(BadCredentialsException.class, () -> {
            useCase.execute(invalidRequest);
        });

        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtTokenProvider, never()).createToken(any());
    }

    @Test
    void execute_InvalidPassword_ThrowsBadCredentialsException() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", testUser.getPassword())).thenReturn(false);

        LoginRequest invalidRequest = new LoginRequest("testuser", "wrongpassword");

        assertThrows(BadCredentialsException.class, () -> {
            useCase.execute(invalidRequest);
        });

        verify(jwtTokenProvider, never()).createToken(any());
    }
}
