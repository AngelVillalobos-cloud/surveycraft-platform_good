package com.surveycraft.service;


import com.surveycraft.entity.User;
import com.surveycraft.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. Método obligatorio de Spring Security
    // Sirve para que Spring sepa buscar al usuario por email cuando intenta loguearse
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Convertimos nuestro User (Entidad) a un UserDetails (Objeto de Spring Security)
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().getNombre().replace("ROLE_", "")) // Spring espera "PLAYER", no "ROLE_PLAYER"
                .build();
    }

    // 2. Método helper para buscar usuario completo (usado por otros servicios)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    // 3. Método para verificar si existe (usado en registro)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
