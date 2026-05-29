-- ============================================================
--  BASE DE DATOS: NIRAMI - Venta de Artesanías
--  Proyecto: nirami
--  Descripción: Sistema de gestión para tienda artesanal
-- ============================================================

CREATE DATABASE IF NOT EXISTS nirami;
USE nirami;

-- ============================================================
-- TABLA: roles
-- Son los roles que va a tener cada usuario y definiran sus permisos 
-- (Administrador, Moderador, Vendedor y cliente)
-- ============================================================

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

-- ============================================================
-- TABLA: usuarios
-- Almacena las cuentas de usuarios registrados en el sistema
-- ============================================================

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_rol INT NOT NULL,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- ============================================================
-- TABLA: correos
-- Almacena los correos de los usuarios
-- ============================================================

CREATE TABLE correos (
    id_correo INT AUTO_INCREMENT PRIMARY KEY,
    correo VARCHAR(100) NOT NULL UNIQUE,
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- TABLA: telefonos
-- Almacena los números de contacto de los usuarios
-- ============================================================

CREATE TABLE telefonos (
    id_telefono INT AUTO_INCREMENT PRIMARY KEY,
    telefono VARCHAR(15) NOT NULL,
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- TABLA: sesiones
-- Registro de inicios de sesión, cierres y control de bloqueos
-- ============================================================

CREATE TABLE sesiones (
    id_sesion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre DATETIME,
    intentos_fallidos INT DEFAULT 0,
    estado ENUM('Activa', 'Cerrada', 'Bloqueada') DEFAULT 'Activa',
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- TABLA: categorias
-- Categorías de productos artesanales disponibles
-- ============================================================

CREATE TABLE categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activa BOOLEAN DEFAULT TRUE
);

-- ============================================================
-- TABLA: productos
-- Catálogo de artesanías. 
-- ============================================================

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    id_vendedor INT NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    precio DECIMAL(12, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    descripcion TEXT,
    materiales VARCHAR(300),
    dimensiones VARCHAR(150),
    estado_moderacion ENUM('Pendiente', 'Aprobado', 'Rechazado', 'Correcion_Solicitada') DEFAULT 'Pendiente',
    disponible BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
    FOREIGN KEY (id_vendedor) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- TABLA: moderacion_productos
-- Historial de revisiones del moderador y motivos de rechazo
-- ============================================================

CREATE TABLE moderacion_productos (
    id_moderacion INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_moderador INT NOT NULL,
    accion ENUM('Revisado', 'Aprobado', 'Rechazado', 'Correccion_solicitada') NOT NULL,
    motivo_observacion TEXT,
    fecha_accion DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_moderador) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- TABLA: notificaciones
-- Alertas del sistema hacia usuarios sobre sus productos o cuenta
-- ============================================================

CREATE TABLE notificaciones (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    titulo VARCHAR(150) NOT NULL,
    mensaje TEXT NOT NULL,
    leida BOOLEAN DEFAULT FALSE,
    fecha_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- TABLA: resenas
-- Permite al usuario comentar y calificar un producto (1-5 estrellas)
-- ============================================================

CREATE TABLE resenas (
    id_resena INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_usuario INT NOT NULL,
    calificacion TINYINT CHECK (calificacion BETWEEN 1 AND 5),
    comentario TEXT,
    fecha_resena DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- ============================================================
-- TABLA: favoritos
-- Productos guardados por los usuarios para futura compra
-- ============================================================

CREATE TABLE favoritos (
    id_favorito INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_producto INT NOT NULL,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (id_usuario, id_producto),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- ============================================================
-- TABLA: recuperacion_contrasena
-- Tokens temporales para restablecer el acceso a la cuenta
-- ============================================================

CREATE TABLE recuperacion_contrasena (
    id_recuperacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    token VARCHAR(255) NOT NULL UNIQUE,
    fecha_expiracion DATETIME NOT NULL,
    usado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);
