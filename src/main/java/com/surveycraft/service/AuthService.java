package com.surveycraft.service;

import com.surveycraft.dto.auth.LoginRequest;
import com.surveycraft.dto.auth.RegisterRequest;
import com.surveycraft.entity.Role;
import com.surveycraft.entity.User;
import com.surveycraft.repository.RoleRepository;
import com.surveycraft.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    // LÓGICA DE REGISTRO
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        String nombreRol = "ROLE_" + request.getRole().toUpperCase();
        Role role = roleRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombreRol));

        User user = new User();
        user.setUsername(request.getUsername()); // Guardamos el nombre real
        user.setEmail(request.getEmail());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
    }

    // LÓGICA DE LOGIN
    public java.util.Map<String, String> login(LoginRequest request) {
        // 1. Autenticar
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // 2. Obtener usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Generar token
        String token = user.getUserId() + ":" + user.getRole().getNombre();

        // 4. VALIDACIÓN DE SEGURIDAD PARA EVITAR EL ERROR NULL
        // Si el username es null en la BD, usamos el email o "Sin Nombre" para que no explote Map.of
        String nombreSeguro = user.getUsername();
        if (nombreSeguro == null) {
            nombreSeguro = "Usuario"; 
        }

        // 5. Retornar respuesta blindada
        return java.util.Map.of(
                "token", token,
                "role", user.getRole().getNombre(),
                "username", nombreSeguro 
        );
    }
}