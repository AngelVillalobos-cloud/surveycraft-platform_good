package com.surveycraft.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "usuarios") // Coincide con tu tabla SQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", length = 20)
    private String username;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    // Mapeamos 'password_hash' a la variable 'password' para que sea más estándar en Java
    @Column(name = "password_hash", length = 60) 
    private String password;

    // --- Relaciones ---

    // Un usuario tiene UN rol
    // SQL: CONSTRAINT fk_usuarios_role FOREIGN KEY (role_id) REFERENCES roless(role_id)
    @ManyToOne(fetch = FetchType.EAGER) // EAGER carga el rol al momento de loguear (necesario para seguridad)
    @JoinColumn(name = "role_id")
    private Role role;

    // --- Constructores ---

    public User() {
    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // --- Getters y Setters ---

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
