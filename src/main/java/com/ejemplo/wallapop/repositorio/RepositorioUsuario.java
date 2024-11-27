package com.ejemplo.wallapop.repositorio;

import com.ejemplo.wallapop.modelo.Usuario;
import com.ejemplo.wallapop.repositorio.RepositorioUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
}