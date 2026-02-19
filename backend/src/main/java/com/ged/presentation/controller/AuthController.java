package com.ged.presentation.controller;

import com.ged.application.dto.request.LoginRequest;
import com.ged.application.dto.response.AuthResponse;
import com.ged.application.usecase.auth.AuthenticateUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authenticateUserUseCase.execute(request);
        return ResponseEntity.ok(response);
    }
}
