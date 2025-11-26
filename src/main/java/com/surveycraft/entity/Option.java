package com.surveycraft.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "optionss") // Coincide con tu tabla SQL (doble 's')
public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long optionId; // Renombrado para coincidir con getOptionId()

    @Column(name = "opcion_texto", length = 20) // Coincide con VARCHAR2(20)
    private String opcionTexto; // Renombrado para coincidir con getOpcionTexto()

    // --- Relaciones ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // --- Constructores ---

    public Option() {
    }

    public Option(String opcionTexto, Question question) {
        this.opcionTexto = opcionTexto;
        this.question = question;
    }

    // --- Getters y Setters ---
    // NOTA: Estos nombres ahora coinciden con los que usamos en SurveyService

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public String getOpcionTexto() {
        return opcionTexto;
    }

    public void setOpcionTexto(String opcionTexto) {
        this.opcionTexto = opcionTexto;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
