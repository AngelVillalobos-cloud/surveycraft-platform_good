package com.surveycraft.dto.survey;

import java.util.List;


public class CreateSurveyRequest {

    private String titulo;
    private String descripcion;
    private Long juegoId;             // ID del videojuego asociado
    private String tipoEncuesta;      // "compra", "analisis", "estado", "general"
    
    // Lo mantenemos para que el frontend no falle al enviarlo, 
    // pero recuerda que no se guarda en la BD (columna eliminada).
    private Boolean esEspecificaJuego; 

    // CAMBIO CLAVE: La lista ahora usa la clase interna 'QuestionRequest'
    private List<QuestionRequest> preguntas;

    // --- Getters y Setters ---

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getJuegoId() {
        return juegoId;
    }

    public void setJuegoId(Long juegoId) {
        this.juegoId = juegoId;
    }

    public String getTipoEncuesta() {
        return tipoEncuesta;
    }

    public void setTipoEncuesta(String tipoEncuesta) {
        this.tipoEncuesta = tipoEncuesta;
    }

    public Boolean getEsEspecificaJuego() {
        return esEspecificaJuego;
    }

    public void setEsEspecificaJuego(Boolean esEspecificaJuego) {
        this.esEspecificaJuego = esEspecificaJuego;
    }

    public List<QuestionRequest> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<QuestionRequest> preguntas) {
        this.preguntas = preguntas;
    }

    // --- Clase interna estática (Renombrada para coincidir con SurveyService) ---
    
    public static class QuestionRequest { // Antes era CreateQuestionRequest
        
        private String texto;
        private String tipoPregunta;   // "single_choice", "rating", etc.
        private List<String> opciones; // ["Sí", "No"]

        // Getters y Setters de la clase interna

        public String getTexto() {
            return texto;
        }

        public void setTexto(String texto) {
            this.texto = texto;
        }

        public String getTipoPregunta() {
            return tipoPregunta;
        }

        public void setTipoPregunta(String tipoPregunta) {
            this.tipoPregunta = tipoPregunta;
        }

        public List<String> getOpciones() {
            return opciones;
        }

        public void setOpciones(List<String> opciones) {
            this.opciones = opciones;
        }
    }
}
