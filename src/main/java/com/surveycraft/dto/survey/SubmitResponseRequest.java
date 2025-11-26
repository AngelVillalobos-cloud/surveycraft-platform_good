package com.surveycraft.dto.survey;

import java.util.List;

public class SubmitResponseRequest {

    // Datos de contexto del jugador para análisis demográfico
    private String tiempoJugadoJuego; // Ej: "0-10h", "10-50h", "50+h"
    private String plataformaUsada; // Ej: "PC", "PS5", "Xbox"

    // Session tracking for big data analytics
    private String sessionId; // Unique session identifier

    // Lista con las selecciones del usuario
    private List<AnswerRequest> respuestas;

    // --- Getters y Setters ---

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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<AnswerRequest> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<AnswerRequest> respuestas) {
        this.respuestas = respuestas;
    }

    // --- Clase interna para cada respuesta individual ---
    public static class AnswerRequest {
        private Long questionId;
        private Long optionId;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public Long getOptionId() {
            return optionId;
        }

        public void setOptionId(Long optionId) {
            this.optionId = optionId;
        }
    }
}
