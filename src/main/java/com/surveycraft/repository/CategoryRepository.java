package com.surveycraft.repository;

import com.surveycraft.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Para buscar categorías por nombre (útil para validaciones)
    boolean existsByNombreCategoria(String nombreCategoria);

    java.util.Optional<Category> findByNombreCategoria(String nombreCategoria);
}
