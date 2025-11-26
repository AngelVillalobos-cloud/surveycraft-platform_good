package com.surveycraft.repository;


import com.surveycraft.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método mágico 1: SELECT * FROM usuarios WHERE email = ?
    // Usado por AuthService para el Login
    Optional<User> findByEmail(String email);

    // Método mágico 2: SELECT COUNT(*) FROM usuarios WHERE email = ?
    // Usado por AuthService para validar que no se repitan correos en el Registro
    boolean existsByEmail(String email);

    // (Opcional) Si alguna vez necesitas buscar por nombre de usuario
    Optional<User> findByUsername(String username);
}
