package com.empresa.inventario.repository;

import com.empresa.inventario.model.Movimiento;
import com.empresa.inventario.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    List<Movimiento> findAllByOrderByFechaHoraDesc();

    List<Movimiento> findByTipoOrderByFechaHoraDesc(TipoMovimiento tipo);

    List<Movimiento> findByTipo(TipoMovimiento tipo);

}
