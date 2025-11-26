package com.surveycraft.repository;

import com.surveycraft.entity.Option;
import com.surveycraft.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResponseRepository extends JpaRepository<Response, Long> {

    // Consultas básicas -----------------------------

    // Busca todas las respuestas hechas por un Player específico
    List<Response> findByPlayer_UserId(Long playerId);

    // Busca todas las respuestas de una pregunta específica
    List<Response> findByQuestion_QuestionId(Long questionId);


    // Consultas personalizadas ----------------------

    // Todas las respuestas pertenecientes a UNA encuesta
    @Query("""
        SELECT r
        FROM Response r
        WHERE r.question.survey.surveyId = :surveyId
    """)
    List<Response> findBySurveyId(@Param("surveyId") Long surveyId);


    // Cuenta total de respuestas enviadas a una encuesta
    @Query("""
        SELECT COUNT(r)
        FROM Response r
        WHERE r.question.survey.surveyId = :surveyId
    """)
    Long countBySurveyId(@Param("surveyId") Long surveyId);


    // Todas las respuestas que un player dio a una encuesta específica
    @Query("""
        SELECT r
        FROM Response r
        WHERE r.player.userId = :playerId
          AND r.question.survey.surveyId = :surveyId
    """)
    List<Response> findByPlayerIdAndSurveyId(
            @Param("playerId") Long playerId,
            @Param("surveyId") Long surveyId
    );

    // Contar *jugadores únicos* que respondieron una encuesta
    @Query("""
        SELECT COUNT(DISTINCT r.player.userId)
        FROM Response r
        WHERE r.question.survey.surveyId = :surveyId
    """)
    Long countUniquePlayersBySurveyId(@Param("surveyId") Long surveyId);

    // Todas las respuestas de un player en una encuesta
    @Query("""
        SELECT r
        FROM Response r
        WHERE r.player.userId = :playerId
          AND r.question.survey.surveyId = :surveyId
    """)
    List<Response> findPlayerResponsesForSurvey(
            @Param("playerId") Long playerId,
            @Param("surveyId") Long surveyId
    );


    // Contar cuántas veces fue elegida una opción
    long countBySelectedOption(Option selectedOption);
}