package com.empresa.inventario.controller;

import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final UsuarioRepository usuarioRepository;

    public LoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ===============================
    // FORMULARIO LOGIN
    // ===============================
    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    // ===============================
    // PROCESAR LOGIN
    // ===============================
    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String correo,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {

        Usuario usuario = usuarioRepository.findByCorreo(correo).orElse(null);

        if (usuario == null || !usuario.getPasswordHash().equals(password)) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login";
        }

        //GUARDAR USUARIO EN SESIÓN
        session.setAttribute("usuario", usuario);

        //REDIRECCIÓN SEGÚN ROL
        String rol = usuario.getRol().getNombre();

        if ("ADMIN".equals(rol)) {
            return "redirect:/admin";
        }

        if ("ALMACENISTA".equals(rol)) {
            return "redirect:/almacen";
        }

        // Seguridad extra
        session.invalidate();
        return "redirect:/login";
    }

    // ===============================
    // LOGOUT
    // ===============================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ===============================
    // ACCESO DENEGADO
    // ===============================
    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "acceso-denegado";
    }
}
