package com.ejemplo.wallapop.controlador;

import com.ejemplo.wallapop.modelo.Usuario;
import com.ejemplo.wallapop.servicio.ServicioUsuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorUsuario {

    @Autowired
    private ServicioUsuario servicioUsuario;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return "registro";
        }
        try {
            servicioUsuario.registrarUsuario(usuario);
        } catch (RuntimeException e) {
            result.rejectValue("email", "error.usuario", "El email ya está registrado");
            return "registro";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login";
    }
}