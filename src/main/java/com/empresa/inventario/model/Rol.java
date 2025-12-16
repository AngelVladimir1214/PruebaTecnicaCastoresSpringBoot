package com.empresa.inventario.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
@Data
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idRol;

    private String nombre;
}
