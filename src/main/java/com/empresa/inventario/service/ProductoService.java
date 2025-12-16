package com.empresa.inventario.service;

import com.empresa.inventario.model.Producto;
import com.empresa.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // ===============================
    // LISTADOS
    // ===============================

    // Usado en INVENTARIO
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // Usado en SALIDAS
    public List<Producto> listarActivos() {
        return productoRepository.findByEstatus(1);
    }

    public List<Producto> listarInactivos() {
        return productoRepository.findByEstatus(0);
    }

    // ===============================
    // CRUD
    // ===============================

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // Crear producto (cantidad inicial 0)
    public Producto guardarProducto(Producto producto) {
        producto.setCantidad(0);
        producto.setEstatus(1);
        return productoRepository.save(producto);
    }

    // Guardado genérico (usado al actualizar cantidad)
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    // ===============================
    // BAJA / REACTIVAR
    // ===============================

    public void darDeBaja(Integer idProducto) {
        Producto producto = buscarPorId(idProducto);
        producto.setEstatus(0);
        productoRepository.save(producto);
    }

    public void reactivar(Integer idProducto) {
        Producto producto = buscarPorId(idProducto);
        producto.setEstatus(1);
        productoRepository.save(producto);
    }
}
