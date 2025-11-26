package com.surveycraft.dto.survey;

import java.util.List;



import java.util.ArrayList;



public class SurveyResultsDTO {

    // Información General
    private Long surveyId;
    private String titulo;
    private String descripcion;
    private Long juegoId;
    
    // --- ¡AQUÍ ESTABA EL ERROR! ---
    // Necesitas declarar esta variable para que el get/set funcionen
    private String estado; 
    // -----------------------------
    
    private String juegoNombre;

    // Datos globales de participación
    private long totalRespuestas; 

    // Las preguntas con sus resultados anidados
    private List<QuestionResultDTO> preguntas = new ArrayList<>();

    // --- Getters y Setters ---
    
    public Long getSurveyId() { return surveyId; }
    public void setSurveyId(Long surveyId) { this.surveyId = surveyId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Long getJuegoId() { return juegoId; }
    public void setJuegoId(Long juegoId) { this.juegoId = juegoId; }

    // --- Getters y Setters para ESTADO ---
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; } // Ahora sí funcionará
    // -------------------------------------

    public long getTotalRespuestas() { return totalRespuestas; }
    public void setTotalRespuestas(long totalRespuestas) { this.totalRespuestas = totalRespuestas; }

    public List<QuestionResultDTO> getPreguntas() { return preguntas; }
    public void setPreguntas(List<QuestionResultDTO> preguntas) { this.preguntas = preguntas; }

    // Getters para nombre
    public String getJuegoNombre() { return juegoNombre; }
    public void setJuegoNombre(String juegoNombre) { this.juegoNombre = juegoNombre; }
    
    // --- Clases internas estáticas ---

    public static class QuestionResultDTO {
        private Long questionId;
        private String textoPregunta;
        private String tipoPregunta;
        private List<OptionResultDTO> opciones = new ArrayList<>();

        public Long getQuestionId() { return questionId; }
        public void setQuestionId(Long questionId) { this.questionId = questionId; }

        public String getTextoPregunta() { return textoPregunta; }
        public void setTextoPregunta(String textoPregunta) { this.textoPregunta = textoPregunta; }

        public String getTipoPregunta() { return tipoPregunta; }
        public void setTipoPregunta(String tipoPregunta) { this.tipoPregunta = tipoPregunta; }

        public List<OptionResultDTO> getOpciones() { return opciones; }
        public void setOpciones(List<OptionResultDTO> opciones) { this.opciones = opciones; }
    }

    public static class OptionResultDTO {
        private Long optionId;
        private String textoOpcion;
        private long votos;       
        private double porcentaje;

        public Long getOptionId() { return optionId; }
        public void setOptionId(Long optionId) { this.optionId = optionId; }

        public String getTextoOpcion() { return textoOpcion; }
        public void setTextoOpcion(String textoOpcion) { this.textoOpcion = textoOpcion; }

        public long getVotos() { return votos; }
        public void setVotos(long votos) { this.votos = votos; }

        public double getPorcentaje() { return porcentaje; }
        public void setPorcentaje(double porcentaje) { this.porcentaje = porcentaje; }
    }
}