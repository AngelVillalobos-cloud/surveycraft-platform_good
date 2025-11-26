package com.surveycraft.repository;

import com.surveycraft.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    
    // Recuperar las opciones (A, B, C) de una pregunta concreta
    List<Option> findByQuestion_QuestionId(Long questionId);
}
