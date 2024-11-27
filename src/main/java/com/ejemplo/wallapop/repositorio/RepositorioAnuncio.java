package com.ejemplo.wallapop.repositorio;

import com.ejemplo.wallapop.modelo.Anuncio;
import com.ejemplo.wallapop.modelo.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAnuncio extends JpaRepository<Anuncio, Long> {
    Page<Anuncio> findAllByOrderByFechaCreacionDesc(Pageable pageable);
    Page<Anuncio> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario, Pageable pageable);
}