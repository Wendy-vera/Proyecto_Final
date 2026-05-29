-- ============================================================
--  BASE DE DATOS: NIRAMI - Venta de Artesanías
-- ============================================================

CREATE DATABASE IF NOT EXISTS nirami_db_completo
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE nirami_db_completo;

-- ── ROLES ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS roles (
    id_rol      INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol  VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- Roles del sistema
INSERT IGNORE INTO roles (id_rol, nombre_rol, descripcion) VALUES
  (1, 'Administrador', 'Control total del sistema: usuarios, productos, categorías, ventas'),
  (2, 'Vendedor',      'Publica y gestiona sus propios productos'),
  (3, 'Cliente',       'Navega el catálogo y realiza compras');

-- USUARIOS
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario     INT AUTO_INCREMENT PRIMARY KEY,
    id_rol         INT NOT NULL,
    nombre_usuario VARCHAR(100) NOT NULL UNIQUE,
    contrasena     VARCHAR(255) NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo         BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- Usuario administrador por defecto  (contraseña: admin123)
INSERT IGNORE INTO usuarios (id_usuario, id_rol, nombre_usuario, contrasena) VALUES
  (1, 1, 'admin', 'admin123');

-- CORREOS 
CREATE TABLE IF NOT EXISTS correos (
    id_correo  INT AUTO_INCREMENT PRIMARY KEY,
    correo     VARCHAR(100) NOT NULL UNIQUE,
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

INSERT IGNORE INTO correos (correo, id_usuario) VALUES
  ('admin@nirami.com', 1);

-- TELÉFONOS
CREATE TABLE IF NOT EXISTS telefonos (
    id_telefono INT AUTO_INCREMENT PRIMARY KEY,
    telefono    VARCHAR(15) NOT NULL,
    id_usuario  INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- SESIONES
CREATE TABLE IF NOT EXISTS sesiones (
    id_sesion       INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT NOT NULL,
    fecha_inicio    DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre    DATETIME,
    intentos_fallidos INT DEFAULT 0,
    estado          ENUM('Activa','Cerrada','Bloqueada') DEFAULT 'Activa',
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- CATEGORÍAS
CREATE TABLE IF NOT EXISTS categorias (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    descripcion  TEXT,
    activa       BOOLEAN DEFAULT TRUE
);

INSERT IGNORE INTO categorias (nombre, descripcion) VALUES
  ('Cerámica',   'Piezas artesanales trabajadas en arcilla y barro'),
  ('Madera',     'Artículos tallados y trabajados en madera'),
  ('Tejidos',    'Manualidades textiles y tejidos a mano'),
  ('Figuras',    'Figuras decorativas y esculturas artesanales'),
  ('Otros',      'Artesanías de diversas técnicas y materiales');

-- PRODUCTOS
CREATE TABLE IF NOT EXISTS productos (
    id_producto       INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria      INT NOT NULL,
    id_vendedor       INT NOT NULL,
    nombre            VARCHAR(150) NOT NULL,
    precio            DECIMAL(12,2) NOT NULL,
    stock             INT NOT NULL DEFAULT 0,
    descripcion       TEXT,
    imagen            VARCHAR(255) NULL
        COMMENT 'Nombre del archivo en assets/imagenes/imgproductos/',
    estado_moderacion ENUM('Pendiente','Aprobado','Rechazado','Correcion_Solicitada') DEFAULT 'Pendiente',
    disponible        BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),
    FOREIGN KEY (id_vendedor)  REFERENCES usuarios(id_usuario)
);

-- MODERACIÓN DE PRODUCTOS
-- (el rol Moderador no existe; estas acciones las realiza el Administrador)
CREATE TABLE IF NOT EXISTS moderacion_productos (
    id_moderacion      INT AUTO_INCREMENT PRIMARY KEY,
    id_producto        INT NOT NULL,
    id_moderador       INT NOT NULL,     -- id del usuario Administrador
    accion             ENUM('Revisado','Aprobado','Rechazado','Correccion_solicitada') NOT NULL,
    motivo_observacion TEXT,
    fecha_accion       DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_producto)  REFERENCES productos(id_producto),
    FOREIGN KEY (id_moderador) REFERENCES usuarios(id_usuario)
);

-- VENTAS
CREATE TABLE IF NOT EXISTS ventas (
    id_venta    INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente  INT NOT NULL,
    fecha_venta DATETIME DEFAULT CURRENT_TIMESTAMP,
    total       DECIMAL(14,2) NOT NULL DEFAULT 0,
    estado      ENUM('Pendiente','Completada','Cancelada') DEFAULT 'Pendiente',
    FOREIGN KEY (id_cliente) REFERENCES usuarios(id_usuario)
);

-- DETALLE DE VENTAS
CREATE TABLE IF NOT EXISTS detalle_ventas (
    id_detalle      INT AUTO_INCREMENT PRIMARY KEY,
    id_venta        INT NOT NULL,
    id_producto     INT NOT NULL,
    cantidad        INT NOT NULL DEFAULT 1,
    precio_unitario DECIMAL(12,2) NOT NULL,
    FOREIGN KEY (id_venta)    REFERENCES ventas(id_venta),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- FAVORITOS 
CREATE TABLE IF NOT EXISTS favoritos (
    id_favorito   INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario    INT NOT NULL,
    id_producto   INT NOT NULL,
    fecha_agregado DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (id_usuario, id_producto),
    FOREIGN KEY (id_usuario)  REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto)
);

-- NOTIFICACIONES
CREATE TABLE IF NOT EXISTS notificaciones (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario      INT NOT NULL,
    titulo          VARCHAR(150) NOT NULL,
    mensaje         TEXT NOT NULL,
    leida           BOOLEAN DEFAULT FALSE,
    fecha_envio     DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

-- RECUPERACIÓN DE CONTRASEÑA
CREATE TABLE IF NOT EXISTS recuperacion_contrasena (
    id_recuperacion  INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario       INT NOT NULL,
    token            VARCHAR(255) NOT NULL UNIQUE,
    fecha_expiracion DATETIME NOT NULL,
    usado            BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario)
);

ALTER TABLE productos
    MODIFY COLUMN imagen VARCHAR(255) NULL
        COMMENT 'Nombre del archivo en assets/imagenes/imgproductos/';

-- ============================================================
--  DATOS DE PRUEBA
-- ============================================================

-- Vendedor de prueba  (contraseña: vendedor123)
INSERT IGNORE INTO usuarios (id_usuario, id_rol, nombre_usuario, contrasena) VALUES
  (2, 2, 'vendedor1', 'vendedor123');
INSERT IGNORE INTO correos (correo, id_usuario) VALUES
  ('vendedor@nirami.com', 2);

-- Cliente de prueba  (contraseña: cliente123)
INSERT IGNORE INTO usuarios (id_usuario, id_rol, nombre_usuario, contrasena) VALUES
  (3, 3, 'cliente1', 'cliente123');
INSERT IGNORE INTO correos (correo, id_usuario) VALUES
  ('cliente@nirami.com', 3);

SELECT * FROM productos;