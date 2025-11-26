package com.surveycraft.entity;

import jakarta.persistence.*;
import java.util.List;


import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId; // <--- CAMBIO: De 'id' a 'questionId'

    @Column(name = "pregunta_texto", length = 50)
    private String preguntaTexto; // <--- CAMBIO: De 'texto' a 'preguntaTexto'

    @Column(name = "tipo_pregunta", length = 20)
    private String tipoPregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options = new ArrayList<>();

    // --- Getters y Setters (Estos son los que busca tu Servicio) ---

    public Long getQuestionId() { // El servicio busca .getQuestionId()
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getPreguntaTexto() { // El servicio busca .getPreguntaTexto()
        return preguntaTexto;
    }

    public void setPreguntaTexto(String preguntaTexto) { // El servicio busca .setPreguntaTexto()
        this.preguntaTexto = preguntaTexto;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(String tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
