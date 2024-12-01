package com.ejemplo.wallapop.repositorio;

import com.ejemplo.wallapop.modelo.Anuncio;
import com.ejemplo.wallapop.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RepositorioAnuncio extends JpaRepository<Anuncio, Long> {
    @Query("SELECT  a FROM Anuncio a join fetch a.usuario where a.id = :id")
    Optional<Anuncio>findByWithUsuarioId(@Param("id") Long id);
    Page<Anuncio> findAllByOrderByFechaCreacionDesc(Pageable pageable);
    Page<Anuncio> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario, Pageable pageable);
}