package com.empresa.inventario.controller;
import jakarta.servlet.http.HttpSession;
import com.empresa.inventario.model.Producto;
import com.empresa.inventario.model.Usuario;
import com.empresa.inventario.service.MovimientoService;
import com.empresa.inventario.service.ProductoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/salidas")
public class SalidaController {

    private final ProductoService productoService;
    private final MovimientoService movimientoService;

    public SalidaController(ProductoService productoService,
                            MovimientoService movimientoService) {
        this.productoService = productoService;
        this.movimientoService = movimientoService;
    }

    // ==================================================
    // LISTAR PRODUCTOS PARA SALIDA (SOLO ALMACENISTA)
    // ==================================================
    @GetMapping
    public String listarProductos(Model model, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        //SOLO ALMACENISTA
        if (!"ALMACENISTA".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        model.addAttribute("productos", productoService.listarActivos());
        return "salidas/lista";
    }

    // ==================================================
    // FORMULARIO DE SALIDA (SOLO ALMACENISTA)
    // ==================================================
    @GetMapping("/{id}")
    public String formularioSalida(@PathVariable Integer id,
                                   Model model,
                                   HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!"ALMACENISTA".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        Producto producto = productoService.buscarPorId(id);

        if (producto.getEstatus() == 0) {
            return "redirect:/salidas";
        }

        model.addAttribute("producto", producto);
        return "salidas/formulario";
    }

    // ==================================================
    // PROCESAR SALIDA (SOLO ALMACENISTA)
    // ==================================================
    @PostMapping("/procesar")
    public String procesarSalida(@RequestParam Integer productoId,
                                 @RequestParam Integer cantidad,
                                 @RequestParam(required = false) String nota,
                                 HttpSession session,
                                 Model model) {

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login";
        }

        if (!"ALMACENISTA".equals(usuario.getRol().getNombre())) {
            return "redirect:/acceso-denegado";
        }

        Producto producto = productoService.buscarPorId(productoId);

        try {
            movimientoService.salidaProducto(
                    producto,
                    usuario,
                    cantidad,
                    nota
            );

            return "redirect:/salidas";

        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("producto", producto);
            return "salidas/formulario";
        }
    }
}
