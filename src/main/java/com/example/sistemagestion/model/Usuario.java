package com.example.sistemagestion.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String email;

    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private Role role;

    @Builder.Default
    @Column(columnDefinition = "boolean default true")
    private boolean enabled = true;

    @Builder.Default
    @Column(columnDefinition = "boolean default true")
    private boolean accountNonLocked = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getNombre()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    // --- MÉTODOS DE SEGURIDAD CONECTADOS A LA BD ---

    @Override
    public boolean isAccountNonExpired() {
        return true; // No se usa, siempre true
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked; // Retorna el valor de la columna en BD (false = baneado)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // No se usa, siempre true
    }

    @Override
    public boolean isEnabled() {
        return this.enabled; // Retorna el valor de la columna en BD (false = inactivo)
    }
}