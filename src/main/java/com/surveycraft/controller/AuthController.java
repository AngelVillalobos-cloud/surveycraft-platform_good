package com.surveycraft.controller;

import com.surveycraft.dto.auth.LoginRequest;
import com.surveycraft.dto.auth.RegisterRequest;
import com.surveycraft.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * POST /api/auth/register
     * Crea un nuevo usuario (Player o Crafter).
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente"));
    }

    /**
     * POST /api/auth/login
     * Recibe email/password y devuelve un JWT y el rol.
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        Map<String, String> response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}