package com.surveycraft.repository;

import com.surveycraft.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // Recuperar todas las preguntas asociadas a una encuesta
    List<Question> findBySurvey_SurveyId(Long surveyId);
}
