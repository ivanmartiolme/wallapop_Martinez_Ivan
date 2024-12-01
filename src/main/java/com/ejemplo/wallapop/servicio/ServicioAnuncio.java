package com.ejemplo.wallapop.servicio;

import com.ejemplo.wallapop.modelo.Anuncio;
import com.ejemplo.wallapop.modelo.Usuario;
import com.ejemplo.wallapop.repositorio.RepositorioAnuncio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServicioAnuncio {

    @Autowired
    private RepositorioAnuncio repositorioAnuncio;

    @Transactional
    public Anuncio guardarAnuncio(Anuncio anuncio) {
        System.out.println("Intentando guardar anuncio: " + anuncio);
        Anuncio anuncioGuardado = repositorioAnuncio.save(anuncio);
        System.out.println("Anuncio guardado con éxito. ID: " + anuncioGuardado.getId());
        return anuncioGuardado;
    }

    public Page<Anuncio> obtenerTodosLosAnuncios(Pageable pageable) {
        return repositorioAnuncio.findAllByOrderByFechaCreacionDesc(pageable);
    }

    public Page<Anuncio> obtenerAnunciosDeUsuario(Usuario usuario, Pageable pageable) {
        return repositorioAnuncio.findByUsuarioOrderByFechaCreacionDesc(usuario, pageable);
    }

    public Anuncio obtenerAnuncioPorId(Long id) {
        return repositorioAnuncio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anuncio no encontrado con id: " + id));
    }

    public void eliminarAnuncio(Anuncio anuncio) {
        repositorioAnuncio.delete(anuncio);
        System.out.println("Anuncio eliminado: " + anuncio.getId());
    }

}