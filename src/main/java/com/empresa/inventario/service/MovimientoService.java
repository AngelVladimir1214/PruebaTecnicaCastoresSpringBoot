package com.empresa.inventario.service;

import com.empresa.inventario.model.*;
import com.empresa.inventario.repository.MovimientoRepository;
import com.empresa.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovimientoService(MovimientoRepository movimientoRepository,
                             ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    // ==================================================
    // ENTRADA DE INVENTARIO
    // ==================================================
    @Transactional
    public void entradaProducto(Producto producto, Usuario usuario, int cantidad, String nota) {

    if (cantidad <= 0) {
        throw new RuntimeException("La cantidad debe ser mayor a cero");
    }

    if (producto.getEstatus() == 0) {
        throw new RuntimeException("No se puede ingresar inventario a un producto inactivo");
    }

        int stockAnterior = producto.getCantidad();
        int stockPosterior = stockAnterior + cantidad;

        producto.setCantidad(stockPosterior);
        productoRepository.save(producto);

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipo(TipoMovimiento.ENTRADA);
        movimiento.setCantidad(cantidad);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockPosterior(stockPosterior);
        movimiento.setNota(nota);

        movimientoRepository.save(movimiento);
    }

    // ==================================================
    // SALIDA DE INVENTARIO
    // ==================================================
    @Transactional
    public void salidaProducto(Producto producto, Usuario usuario, int cantidad, String nota) {

    if (cantidad <= 0) {
        throw new RuntimeException("La cantidad debe ser mayor a cero");
    }

    if (producto.getEstatus() == 0) {
        throw new RuntimeException("El producto está inactivo");
    }

    if (producto.getCantidad() < cantidad) {
        throw new RuntimeException("No hay suficiente inventario disponible");
    }

        int stockAnterior = producto.getCantidad();
        int stockPosterior = stockAnterior - cantidad;

        producto.setCantidad(stockPosterior);
        productoRepository.save(producto);

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setUsuario(usuario);
        movimiento.setTipo(TipoMovimiento.SALIDA);
        movimiento.setCantidad(cantidad);
        movimiento.setStockAnterior(stockAnterior);
        movimiento.setStockPosterior(stockPosterior);
        movimiento.setNota(nota);

        movimientoRepository.save(movimiento);
    }

    // ==================================================
    // HISTORIAL DE MOVIMIENTOS
    // ==================================================
    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAllByOrderByFechaHoraDesc();
    }

    public List<Movimiento> listarPorTipo(TipoMovimiento tipo) {
        return movimientoRepository.findByTipoOrderByFechaHoraDesc(tipo);
    }


}
