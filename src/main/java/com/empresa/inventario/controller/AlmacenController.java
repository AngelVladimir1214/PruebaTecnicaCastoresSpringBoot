package com.empresa.inventario.controller;

import com.empresa.inventario.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/almacen")
public class AlmacenController {

    @GetMapping
    public String dashboard(HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null || !"ALMACENISTA".equals(usuario.getRol().getNombre())) {
            return "redirect:/login";
        }

        return "almacen/dashboard";
    }
}
