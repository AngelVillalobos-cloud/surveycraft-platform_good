package com.surveycraft.repository;


import com.surveycraft.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    // Método mágico: SELECT * FROM roless WHERE nombre = ?
    // Usado en el Registro: El frontend manda "ROLE_PLAYER", y esto busca el ID (ej. 2)
    Optional<Role> findByNombre(String nombre);
}
