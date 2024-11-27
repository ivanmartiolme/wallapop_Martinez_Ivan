package com.ejemplo.wallapop.controlador;

import com.ejemplo.wallapop.modelo.Anuncio;
import com.ejemplo.wallapop.modelo.Usuario;
import com.ejemplo.wallapop.servicio.ServicioAnuncio;
import com.ejemplo.wallapop.servicio.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControladorPrincipal {

    @Autowired
    private ServicioAnuncio servicioAnuncio;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @GetMapping("/")
    public String inicio(Model model, @RequestParam(defaultValue = "0") int pagina) {
        Page<Anuncio> anuncios = servicioAnuncio.obtenerTodosLosAnuncios(PageRequest.of(pagina, 10));
        model.addAttribute("anuncios", anuncios);
        return "inicio";
    }

    @GetMapping("/mis-anuncios")
    public String misAnuncios(@AuthenticationPrincipal Usuario usuario, Model model, @RequestParam(defaultValue = "0") int pagina) {
        Page<Anuncio> anuncios = servicioAnuncio.obtenerAnunciosDeUsuario(usuario, PageRequest.of(pagina, 5));
        model.addAttribute("anuncios", anuncios);
        return "mis-anuncios";
    }
}