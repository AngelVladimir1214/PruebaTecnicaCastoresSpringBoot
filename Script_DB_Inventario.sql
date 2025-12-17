USE db_inventario;
/*
-- ============================================
-- Tabla ROLES
-- ============================================
CREATE TABLE roles (
    idRol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- ============================================
-- Tabla USUARIOS
-- ============================================
CREATE TABLE usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    rol_id INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES roles(idRol)
);

-- ============================================
-- Tabla PRODUCTOS
-- ============================================
CREATE TABLE productos (
    idProducto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL DEFAULT 0,
    estatus TINYINT(1) NOT NULL DEFAULT 1, -- 1=activo, 0=inactivo
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- Tabla MOVIMIENTOS
-- ============================================
CREATE TABLE movimientos (
    idMovimiento INT AUTO_INCREMENT PRIMARY KEY,
    producto_id INT NOT NULL,
    usuario_id INT NOT NULL,
    tipo ENUM('ENTRADA', 'SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    nota VARCHAR(255),
    stock_anterior INT NOT NULL,
    stock_posterior INT NOT NULL,
    
    FOREIGN KEY (producto_id) REFERENCES productos(idProducto),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(idUsuario)
);
*/


/*
-- =================================================
-- PROCEDIMIENTOS PARA ENTRADA O SALIDA
-- =================================================
	DELIMITER $$

CREATE PROCEDURE sp_registrar_movimiento(
    IN p_producto_id INT,
    IN p_usuario_id INT,
    IN p_tipo ENUM('ENTRADA','SALIDA'),
    IN p_cantidad INT,
    IN p_nota VARCHAR(255)
)
BEGIN
    DECLARE v_stock_actual INT;
    DECLARE v_stock_nuevo INT;

    SELECT cantidad INTO v_stock_actual
    FROM productos
    WHERE idProducto = p_producto_id
    FOR UPDATE;

    IF v_stock_actual IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El producto no existe.';
    END IF;

    IF p_cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cantidad debe ser mayor a cero.';
    END IF;

    IF p_tipo = 'ENTRADA' THEN
        SET v_stock_nuevo = v_stock_actual + p_cantidad;
    ELSEIF p_tipo = 'SALIDA' THEN
        IF p_cantidad > v_stock_actual THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No hay suficiente inventario.';
        END IF;
        SET v_stock_nuevo = v_stock_actual - p_cantidad;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de movimiento inválido.';
    END IF;

    UPDATE productos
    SET cantidad = v_stock_nuevo
    WHERE idProducto = p_producto_id;

    INSERT INTO movimientos (producto_id, usuario_id, tipo, cantidad, nota, stock_anterior, stock_posterior)
    VALUES (p_producto_id, p_usuario_id, p_tipo, p_cantidad, p_nota, v_stock_actual, v_stock_nuevo);

END $$

DELIMITER ;
*/



/*

-- =======================================
-- INSERTAR ROLES DE ADMINISTRADOR Y ALMACENISTA
-- =======================================
INSERT INTO roles (nombre)
VALUES ('ADMIN'), ('ALMACENISTA');


-- =======================================
-- INSERTAR USUARIO CON ROL DE ADMIN Y DE ALMACENISTA
-- =======================================
INSERT INTO usuarios (password_hash, nombre, email, rol_id)
VALUES 
('Admin123', 'Administrador', 'admin@correo.com', 1),
('Almacenista123', 'Almacenista', 'almacen@correo.com', 2);

-- =======================================
-- INSERTAR PRODUCTOS DE EJEMPLO
-- =======================================
INSERT INTO productos (nombre, precio)
VALUES
('Laptop', 3000.00),
('PC', 4000.00),
('MOUSE', 100.00),
('MONITOR', 2000.00),
('MICROFONO', 350.00),
('AUDIFONOS', 450.00);
*/



/*
-- =================================================
-- PROCEDIMIENTOS PARA ENTRADA O SALIDA
-- =================================================
	DELIMITER $$

CREATE PROCEDURE sp_registrar_movimiento(
    IN p_producto_id INT,
    IN p_usuario_id INT,
    IN p_tipo ENUM('ENTRADA','SALIDA'),
    IN p_cantidad INT,
    IN p_nota VARCHAR(255)
)
BEGIN
    DECLARE v_stock_actual INT;
    DECLARE v_stock_nuevo INT;

    SELECT cantidad INTO v_stock_actual
    FROM productos
    WHERE idProducto = p_producto_id
    FOR UPDATE;

    IF v_stock_actual IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El producto no existe.';
    END IF;

    IF p_cantidad <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La cantidad debe ser mayor a cero.';
    END IF;

    IF p_tipo = 'ENTRADA' THEN
        SET v_stock_nuevo = v_stock_actual + p_cantidad;
    ELSEIF p_tipo = 'SALIDA' THEN
        IF p_cantidad > v_stock_actual THEN
            SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'No hay suficiente inventario.';
        END IF;
        SET v_stock_nuevo = v_stock_actual - p_cantidad;
    ELSE
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Tipo de movimiento inválido.';
    END IF;

    UPDATE productos
    SET cantidad = v_stock_nuevo
    WHERE idProducto = p_producto_id;

    INSERT INTO movimientos (producto_id, usuario_id, tipo, cantidad, nota, stock_anterior, stock_posterior)
    VALUES (p_producto_id, p_usuario_id, p_tipo, p_cantidad, p_nota, v_stock_actual, v_stock_nuevo);

END $$

DELIMITER ;
*/




SELECT * from roles	;
