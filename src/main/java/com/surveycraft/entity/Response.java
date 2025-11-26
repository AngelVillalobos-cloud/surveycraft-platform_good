package com.surveycraft.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "responses")
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "response_id")
    private Long responseId;

    // --- Relaciones ---

    // El Player que respondió (Usuario)
    // SQL: CONSTRAINT fk_responses_player FOREIGN KEY (player_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id")
    private User player;

    // La pregunta que se respondió
    // SQL: CONSTRAINT fk_responses_question FOREIGN KEY (question_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // La opción que eligió el usuario
    // SQL: CONSTRAINT fk_responses_option FOREIGN KEY (option_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option selectedOption;

    // --- Datos adicionales (Calidad de datos) ---

    // SQL: tiempo_jugado VARCHAR2(20)
    @Column(name = "tiempo_jugado", length = 20)
    private String tiempoJugadoJuego;

    // SQL: plataforma_usada VARCHAR2(20)
    @Column(name = "plataforma_usada", length = 20)
    private String plataformaUsada;

    // --- Big Data Analytics Fields ---

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "session_id", length = 50)
    private String sessionId;

    // --- Constructores ---

    public Response() {
    }

    // --- Getters y Setters ---

    public Long getResponseId() {
        return responseId;
    }

    public void setResponseId(Long responseId) {
        this.responseId = responseId;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Option getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(Option selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getTiempoJugadoJuego() {
        return tiempoJugadoJuego;
    }

    public void setTiempoJugadoJuego(String tiempoJugadoJuego) {
        this.tiempoJugadoJuego = tiempoJugadoJuego;
    }

    public String getPlataformaUsada() {
        return plataformaUsada;
    }

    public void setPlataformaUsada(String plataformaUsada) {
        this.plataformaUsada = plataformaUsada;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}