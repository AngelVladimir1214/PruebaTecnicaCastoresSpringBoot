package com.empresa.inventario.controller;

import com.empresa.inventario.model.Producto;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.MovimientoService;
import com.empresa.inventario.service.ProductoService;
import com.empresa.inventario.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final ProductoService productoService;
    private final MovimientoService movimientoService;
    private final UsuarioService usuarioService;

    public InventarioController(ProductoService productoService,
                                MovimientoService movimientoService,
                                UsuarioService usuarioService) {
        this.productoService = productoService;
        this.movimientoService = movimientoService;
        this.usuarioService = usuarioService;
    }

    // ==================================================
    // LISTAR INVENTARIO (ADMIN y ALMACENISTA)
    // ==================================================
    @GetMapping
    public String listarInventario(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        // Ambos roles pueden ver inventario
        model.addAttribute("productos", productoService.listarTodos());
        model.addAttribute("usuario", usuario);

        return "inventario/lista";
    }

    // ==================================================
    // FORMULARIO DE CREAR PRODUCTO (SOLO ADMIN)
    // ==================================================
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoProducto(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        //Restricción por rol
        if (!"ADMIN".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("producto", new Producto());
        return "inventario/nuevo";
    }

    // ==================================================
    // CREAR NUEVO PRODUCTO (SOLO ADMIN)
    // ==================================================
    @PostMapping("/nuevo")
    public String crearProducto(@ModelAttribute Producto producto, HttpSession session, Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        //Restricción por rol
        if (!"ADMIN".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        try {
            productoService.guardarProducto(producto);
        } catch (Exception e) {
            model.addAttribute("error", "Error al crear el producto: " + e.getMessage());
            return "inventario/nuevo";
        }

        return "redirect:/inventario";
    }

    // ==================================================
    // FORMULARIO DE ENTRADA (SOLO ADMIN)
    // ==================================================
    @GetMapping("/entrada/{id}")
    public String mostrarEntrada(@PathVariable Integer id,
                                 Model model,
                                 HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        //Restricción por rol
        if (!"ADMIN".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);

        return "inventario/entrada";
    }

    // ==================================================
    // PROCESAR ENTRADA (SOLO ADMIN)
    // ==================================================
    @PostMapping("/entrada")
    public String procesarEntrada(@RequestParam Integer productoId,
                                  @RequestParam Integer cantidad,
                                  @RequestParam(required = false) String nota,
                                  HttpSession session,
                                  Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        //Restricción por rol
        if (!"ADMIN".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        Producto producto = productoService.buscarPorId(productoId);

        try {
            movimientoService.entradaProducto(
                    producto,
                    usuario,
                    cantidad,
                    nota
            );
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("producto", producto);
            return "inventario/entrada";
        }

        return "redirect:/inventario";
    }

    // ===============================
// DAR DE BAJA (ADMIN)
// ===============================
@GetMapping("/baja/{id}")
public String darDeBaja(@PathVariable Integer id, HttpSession session) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/login";

    if (!"ADMIN".equals(usuario.getRol().getNombre())) {
        return "redirect:/acceso-denegado";
    }

    productoService.darDeBaja(id);
    return "redirect:/inventario";
}

// ===============================
// REACTIVAR (ADMIN)
// ===============================
@GetMapping("/reactivar/{id}")
public String reactivar(@PathVariable Integer id, HttpSession session) {

    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null) return "redirect:/login";

    if (!"ADMIN".equals(usuario.getRol().getNombre())) {
        return "redirect:/acceso-denegado";
    }

    productoService.reactivar(id);
    return "redirect:/inventario";
}

}
