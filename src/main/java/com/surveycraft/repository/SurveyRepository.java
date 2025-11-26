package com.surveycraft.repository;

import com.surveycraft.entity.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    // Filtrar encuestas por estado (ej: 'activa')
    List<Survey> findByEstado(String estado);

    // Ver encuestas creadas por un Crafter específico
    List<Survey> findByCrafter_UserId(Long userId);

    // Buscar por tipo (compra, analisis, etc.)
    List<Survey> findByTipoEncuesta(String tipoEncuesta);

    // Obtener encuestas de un juego específico
    // Asumiendo que en la entidad Survey la propiedad se llama 'juego'
    @Query("SELECT s FROM Survey s WHERE s.juegoId = :juegoId")
    List<Survey> findByJuegoId(@Param("juegoId") Long juegoId);
}
