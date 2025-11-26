package com.surveycraft.repository;

import com.surveycraft.entity.Videojuego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {

    // CORRECCIÓN: Usamos "Category_Id"
    // "Category" = nombre del campo en la entidad Videojuego
    // "_" = navega dentro del objeto Category
    // "Id" = nombre del campo ID dentro de Category
    List<Videojuego> findByCategoria_Id(Long id);

    // Buscador por título (Este está perfecto)
    List<Videojuego> findByTituloContainingIgnoreCase(String titulo);

    // Para DataInitializer
    Optional<Videojuego> findByTitulo(String titulo);
}
