-- ============================================================
--  MIGRACIÓN: agregar columna imagen a la tabla productos
--  Ejecutar UNA VEZ sobre la base de datos nirami
-- ============================================================

USE nirami_db_completo;

ALTER TABLE productos
    ADD COLUMN imagen VARCHAR(255) NULL
        COMMENT 'Nombre del archivo de imagen guardado en assets/imagenes/imgproductos/';
