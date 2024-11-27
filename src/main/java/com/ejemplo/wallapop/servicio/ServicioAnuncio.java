package com.ejemplo.wallapop.servicio;

import com.ejemplo.wallapop.modelo.Anuncio;
import com.ejemplo.wallapop.modelo.Usuario;
import com.ejemplo.wallapop.repositorio.RepositorioAnuncio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServicioAnuncio {

    @Autowired
    private RepositorioAnuncio repositorioAnuncio;

    @Transactional
    public Anuncio guardarAnuncio(Anuncio anuncio) {
        System.out.println("Intentando guardar anuncio en la base de datos");
        try {
            Anuncio anuncioGuardado = repositorioAnuncio.save(anuncio);
            System.out.println("Anuncio guardado con éxito. ID: " + anuncioGuardado.getId());
            return anuncioGuardado;
        } catch (Exception e) {
            System.err.println("Error al guardar el anuncio: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Page<Anuncio> obtenerTodosLosAnuncios(Pageable pageable) {
        return repositorioAnuncio.findAllByOrderByFechaCreacionDesc(pageable);
    }

    public Page<Anuncio> obtenerAnunciosDeUsuario(Usuario usuario, Pageable pageable) {
        return repositorioAnuncio.findByUsuarioOrderByFechaCreacionDesc(usuario, pageable);
    }

    /*public Anuncio guardarAnuncio(Anuncio anuncio) {
        return repositorioAnuncio.save(anuncio);
    }*/


    public Anuncio obtenerAnuncioPorId(Long id) {
        return repositorioAnuncio.findById(id)
                .orElseThrow(() -> new RuntimeException("Anuncio no encontrado"));
    }

    public void eliminarAnuncio(Anuncio anuncio) {
        repositorioAnuncio.delete(anuncio);
        System.out.println("Anuncio eliminado: " + anuncio.getId());
    }

}