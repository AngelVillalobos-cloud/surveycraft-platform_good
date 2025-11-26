package com.surveycraft.entity;

import jakarta.persistence.*; // Si usas una versión vieja de Spring Boot, cambia a javax.persistence.*
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "surveys")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Long surveyId;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "estado", length = 20)
    private String estado;

    // En tu SQL, 'es_especifica_juego' está comentado, así que NO se mapea aquí.

    @Column(name = "juego_id")
    private Long juegoId; // Mapeamos la columna directamente para facilitar tu lógica actual

    @Column(name = "tipo_encuesta", length = 20)
    private String tipoEncuesta;

    // --- Relaciones (Foreign Keys) ---

    // SQL: CONSTRAINT fk_surveys_crafter FOREIGN KEY (crafter_id) REFERENCES
    // usuarios(user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crafter_id")
    private User crafter;

    // Relación inversa con Questions (One Survey -> Many Questions)
    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    // --- Constructores ---

    public Survey() {
    }

    // --- Getters y Setters ---

    public Long getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Long surveyId) {
        this.surveyId = surveyId;
    }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    public User getCrafter() {
        return crafter;
    }

    public void setCrafter(User crafter) {
        this.crafter = crafter;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    // Método de utilidad para el Controller (ya que borramos el campo de la DB)
    // Esto permite saber si es específica sin tener la columna en la tabla
    public Boolean getEsEspecificaJuego() {
        return this.juegoId != null;
    }
}
