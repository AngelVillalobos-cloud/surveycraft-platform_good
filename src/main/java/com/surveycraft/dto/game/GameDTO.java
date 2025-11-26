package com.surveycraft.dto.game;


public class GameDTO {

	private Long id;
    private String categoriaNombre; // Ej: "RPG de Acción"

    // --- Campos de Entrada y Salida ---
    private String titulo;
    private String descripcion;
    
    // --- Campo de Entrada (Enviado por el Frontend al crear) ---
    private Long categoriaId; // Ej: 4
	
	
    public GameDTO() {
    	
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }
}
