package com.empresa.inventario.controller;

import com.empresa.inventario.model.TipoMovimiento;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.MovimientoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public String listarMovimientos(
            @RequestParam(required = false) TipoMovimiento tipo,
            Model model,
            HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        //Validar sesión
        if (usuario == null) {
            return "redirect:/login";
        }

        //SOLO ADMIN
        if (!"ADMIN".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        if (tipo != null) {
            model.addAttribute(
                    "movimientos",
                    movimientoService.listarPorTipo(tipo)
            );
        } else {
            model.addAttribute(
                    "movimientos",
                    movimientoService.listarTodos()
            );
        }

        return "movimientos/lista";
    }
}
