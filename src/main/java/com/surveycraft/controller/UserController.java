package com.surveycraft.controller;


import com.surveycraft.entity.User;
import com.surveycraft.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET /api/users/me
     * Obtiene el perfil del usuario actualmente autenticado (basado en su Token).
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserProfile(Authentication authentication) {
        // Spring Security inyecta el objeto 'authentication' automáticamente
        String email = authentication.getName(); 
        
        // Buscamos al usuario completo en la BD
        User user = userService.findByEmail(email);
        
        // Devolvemos solo los datos seguros (evitar devolver el hash de la contraseña)
        return ResponseEntity.ok(Map.of(
            "userId", user.getUserId(),
            "username", user.getUsername(),
            "email", user.getEmail(),
            "role", user.getRole().getNombre() // Asumiendo que Role tiene getNombre()
        ));
    }
}