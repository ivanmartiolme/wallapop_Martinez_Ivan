package com.ejemplo.wallapop.controlador;

import com.ejemplo.wallapop.modelo.Anuncio;
import com.ejemplo.wallapop.modelo.Imagen;
import com.ejemplo.wallapop.modelo.Usuario;
import com.ejemplo.wallapop.repositorio.RepositorioAnuncio;
import com.ejemplo.wallapop.servicio.ServicioAnuncio;
import com.ejemplo.wallapop.servicio.ServicioImagen;
import com.ejemplo.wallapop.servicio.ServicioUsuario;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/anuncios")
public class ControladorAnuncio {

    @Autowired
    private ServicioAnuncio servicioAnuncio;

    @Autowired
    private ServicioUsuario servicioUsuario;

    @Autowired
    private ServicioImagen servicioImagen;

    @GetMapping
    public String listarAnuncios(@RequestParam(defaultValue = "0") int pagina, Model model) {
        Page<Anuncio> anuncios = servicioAnuncio.obtenerTodosLosAnuncios(PageRequest.of(pagina, 10));
        model.addAttribute("anuncios", anuncios);
        return "lista-anuncios";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoAnuncio(Model model) {
        model.addAttribute("anuncio", new Anuncio());
        return "formulario-anuncio";
    }

    @PostMapping("/nuevo")
    public String crearAnuncio(@Valid @ModelAttribute Anuncio anuncio,
                               BindingResult result,
                               @RequestParam("imagenes") List<MultipartFile> imagenes,
                               Authentication authentication,
                               Model model) throws IOException {
        if (result.hasErrors()) {
            return "formulario-anuncio";
        }
        Usuario usuario = servicioUsuario.obtenerUsuarioPorEmail(authentication.getName());
        if (usuario == null) {
            return "redirect:/error";
        }
        anuncio.setUsuario(usuario);

        try {
            for (MultipartFile archivo : imagenes) {
                if (!archivo.isEmpty()) {
                    Imagen nuevaImagen = servicioImagen.guardarImagen(archivo);
                    anuncio.addImagen(nuevaImagen);
                }
            }

            servicioAnuncio.guardarAnuncio(anuncio);
            return "redirect:/anuncios/mis-anuncios";
        } catch (Exception e) {
            model.addAttribute("error", "Ocurrió un error al guardar el anuncio. Por favor, inténtelo de nuevo.");
            return "formulario-anuncio";
        }
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarAnuncio(@PathVariable Long id, Model model, Authentication authentication) {
        Anuncio anuncio = servicioAnuncio.obtenerAnuncioPorId(id);
        if (!anuncio.getUsuario().getEmail().equals(authentication.getName())) {
            return "redirect:/anuncios";
        }
        model.addAttribute("anuncio", anuncio);
        return "formulario-anuncio";
    }

    @PostMapping("/editar/{id}")
    public String editarAnuncio(@PathVariable Long id, @Valid @ModelAttribute Anuncio anuncio,
                                BindingResult result, @RequestParam("imagenes") List<MultipartFile> imagenes,
                                Authentication authentication) throws IOException {
        if (result.hasErrors()) {
            return "formulario-anuncio";
        }

        Anuncio anuncioExistente = servicioAnuncio.obtenerAnuncioPorId(id);
        if (!anuncioExistente.getUsuario().getEmail().equals(authentication.getName())) {
            return "redirect:/anuncios";
        }

        anuncioExistente.setTitulo(anuncio.getTitulo());
        anuncioExistente.setPrecio(anuncio.getPrecio());
        anuncioExistente.setDescripcion(anuncio.getDescripcion());

        for (MultipartFile imagen : imagenes) {
            if (!imagen.isEmpty()) {
                Imagen nuevaImagen = servicioImagen.guardarImagen(imagen);
                anuncioExistente.addImagen(nuevaImagen);
            }
        }

        servicioAnuncio.guardarAnuncio(anuncioExistente);
        return "redirect:/";
    }

    @SneakyThrows
    @PostMapping("/borrar/{id}")
    public String borrarAnuncio(@PathVariable Long id, Authentication authentication) {
        Anuncio anuncio = servicioAnuncio.obtenerAnuncioPorId(id);

        if (!anuncio.getUsuario().getEmail().equals(authentication.getName())) {
            return "redirect:/anuncios";
        }

        for (Imagen imagen : anuncio.getImagenes()) {
            servicioImagen.eliminarImagen(imagen.getRuta());
        }

        servicioAnuncio.eliminarAnuncio(anuncio);

        return "redirect:/anuncios";
    }


    @GetMapping("/ver/{id}")
    public String verAnuncio(@PathVariable Long id, Model model) {
        try {
            Anuncio anuncio = servicioAnuncio.obtenerAnuncioPorId(id);
            if (anuncio.getUsuario() == null) {
                anuncio.setUsuario(new Usuario());
            }
            model.addAttribute("anuncio", anuncio);
            return "ver-anuncio";
        } catch (Exception e) {
            model.addAttribute("error", "No se pudo encontrar el anuncio: " + e.getMessage());
            return "error";
        }
    }

    @GetMapping("/mis-anuncios")
    public String listarMisAnuncios(@RequestParam(defaultValue = "0") int pagina, Model model, Authentication authentication) {
        UserDetails usuario = servicioUsuario.obtenerUsuarioPorEmail(authentication.getName());
        Page<Anuncio> anuncios = servicioAnuncio.obtenerAnunciosDeUsuario((Usuario) usuario, PageRequest.of(pagina, 10));
        model.addAttribute("anuncios", anuncios);
        return "mis-anuncios";
    }
}