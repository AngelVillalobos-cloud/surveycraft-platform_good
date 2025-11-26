package com.surveycraft.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "roless") // ¡Ojo! Coincide con tu SQL (doble 's')
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", sequenceName = "ROLE_SEQ", allocationSize = 1)
    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "nombre", length = 20, nullable = false)
    private String nombre; // Ej: "ROLE_CRAFTER" o "ROLE_PLAYER"

    @Column(name = "descripcion", length = 50)
    private String descripcion;

    // --- Constructores ---

    public Role() {
    }

    public Role(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // --- Getters y Setters ---

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
